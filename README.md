📱 Kreeda-Ankana
Village Sports Ground & Match Organizer (Android App)

Kreeda-Ankana is an Android application designed to help village sports communities easily book playgrounds, organize matches, post challenges, and record match scores — with full offline support and real-time synchronization.

🌟 Features

🔹 1. Ground Booking & Slot Management
View all local playgrounds
Check availability in a calendar view
Book time slots with conflict-free transactions

Offline support with automatic sync
🔹 2. Challenge Board
Post match challenges
Reply to challenges in real-time
Community-driven interaction using Firebase listeners

🔹 3. Score Wall
Publish match results
View historical match scores
Simple, clean interface for quick updates

🔹 4. Authentication
Email login
Anonymous guest access
Basic user profile

🔹 5. Offline-First Experience
Uses Room database for local caching
Syncs automatically when network is available
Works smoothly in low-connectivity rural areas🏛️ Tech Stack
Kotlin (Android)
Firebase Firestore – real-time database
Firebase Auth – authentication
Firebase Storage – media uploads
Room Database – offline caching
Hilt – dependency injection
Coroutines / Flow – async operations📊 Key Modules
Grounds Module → list, calendar view, bookings
Challenges Module → posts, replies
Scores Module → match results
Auth Module → login, profile
Sync Module → Firestore ↔ Room sync
🚀 How to Run the Project

Clone the repository:

git clone https://github.com/your-username/kreeda-ankana.git
Open the project in Android Studio (Arctic Fox or newer)

Add your Firebase google-services.json under:

app/src/main/

Enable:
Firestore
Authentication
Firebase Storage
Build & run the app on an Android device/emulator

🛡️ Security
Firestore rules for booking protection
Validation to prevent double booking
Auth-based access control
Local data encryption (optional)
MVVM + Clean Architecture

📊 Key Modules
Grounds Module → list, calendar view, bookings

Challenges Module → posts, replies

Scores Module → match results

Auth Module → login, profile

Sync Module → Firestore ↔ Room sync

🚀 How to Run the Project

Clone the repository:

git clone https://github.com/your-username/kreeda-ankana.git
Open the project in Android Studio (Arctic Fox or newer)

Add your Firebase google-services.json under:

app/src/main/
Enable:
Firestore
Authentication
Firebase Storage
Build & run the app on an Android device/emulator
🛡️ Security
Firestore rules for booking protection
Validation to prevent double booking
Auth-based access control🙌 Contributing

Pull requests are welcome!
If you'd like to contribute features or improvements, feel free to open an issue.

📜 License

This project is licensed under the MIT License.

💬 Author

Prajwal Naik
Final-year CSE Student
Android Developer | AI Enthusiast
