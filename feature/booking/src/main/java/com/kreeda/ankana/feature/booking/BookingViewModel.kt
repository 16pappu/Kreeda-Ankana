package com.kreeda.ankana.feature.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreeda.ankana.core.data.repository.BookingRepository
import com.kreeda.ankana.core.data.repository.GroundsRepository
import com.kreeda.ankana.core.model.Booking
import com.kreeda.ankana.core.model.Slot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.kreeda.ankana.core.network.WeatherService

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val groundsRepository: GroundsRepository,
    private val weatherService: WeatherService
) : ViewModel() {

    val bookings: StateFlow<List<Booking>> = bookingRepository.observeBookings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val slots: StateFlow<List<Slot>> = groundsRepository.observeSlots("ground_01")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _slotWeather = kotlinx.coroutines.flow.MutableStateFlow<Map<String, String>>(emptyMap())
    val slotWeather: StateFlow<Map<String, String>> = _slotWeather

    private val _errorMessages = kotlinx.coroutines.flow.MutableSharedFlow<String>()
    val errorMessages: kotlinx.coroutines.flow.SharedFlow<String> = _errorMessages

    init {
        fetchWeatherForSlots()
    }

    private fun fetchWeatherForSlots() {
        viewModelScope.launch {
            weatherService.getWeatherForecast().onSuccess { response ->
                val weatherMap = mutableMapOf<String, String>()
                val slotsList = groundsRepository.observeSlots("ground_01").stateIn(viewModelScope).value
                // For simplicity, assign a random weather emoji based on the forecast list if we have one
                // OpenWeatherMap returns forecasts every 3 hours. We'll just map the icons.
                val availableIcons = response.list.map { it.weather.firstOrNull()?.icon }.filterNotNull()
                
                // We'll just fake mapping them sequentially to slots to show the UI
                slots.collect { currentSlots ->
                    currentSlots.forEachIndexed { index, slot ->
                        val iconCode = availableIcons.getOrNull(index % availableIcons.size) ?: "01d"
                        val emoji = when {
                            iconCode.startsWith("01") -> "☀️" // Clear
                            iconCode.startsWith("02") || iconCode.startsWith("03") || iconCode.startsWith("04") -> "☁️" // Clouds
                            iconCode.startsWith("09") || iconCode.startsWith("10") -> "🌧️" // Rain
                            iconCode.startsWith("11") -> "⛈️" // Thunderstorm
                            iconCode.startsWith("13") -> "❄️" // Snow
                            iconCode.startsWith("50") -> "🌫️" // Mist
                            else -> "⛅"
                        }
                        weatherMap[slot.slotId] = emoji
                    }
                    _slotWeather.value = weatherMap
                }
            }
        }
    }

    fun createBooking(slotId: String, teamName: String, sportType: String) {
        viewModelScope.launch {
            val userBookings = bookings.value.filter { it.userUid == "local_demo_user" }
            if (userBookings.size >= 2) {
                _errorMessages.emit("Cannot book more than 2 hours per day.")
                return@launch
            }
            bookingRepository.createBooking(
                bookingId = "booking_${System.currentTimeMillis()}",
                groundId = "ground_01",
                slotId = slotId,
                userUid = "local_demo_user",
                teamName = teamName,
                sportType = sportType
            )
        }
    }
}
