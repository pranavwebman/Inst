# InstaEducate 🎓
> **Tagline:** “Scroll. Learn. Grow.”  
> A modern social learning Android platform combining the high-engagement vertical layout of Instagram with premium study and active recall features.

---

## 🎨 Design & Aesthetic System
InstaEducate is designed with visuals in mind:
- **AMOLED Dark Mode**: True deep-black interface designed for modern OLED screens to reduce eye fatigue and save battery.
- **Glassmorphic Cards**: Frosted semi-transparent panels utilizing subtle dynamic gradients and custom Compose modifiers.
- **Micro-Animations**: Snappy state feedback, heart scaling animations on double-tap, and sliding timers.

---

## 🚀 Key Features

1. **Daily Study Streak Flame**: Visual daily streaking indicator keeping students accountable and incentivizing active recall.
2. **Horizontal Categories Feed**: Instantly filter content into Math, Coding, AI, Science, and Exam Preparation cards.
3. **Multi-Format Study Cards**:
   - **Quiz Cards**: Answer interactive multiple-choice questions with instant correct/incorrect color feedback and concept explanations.
   - **Coding Snippets**: Fenced code block widgets formatted in Kotlin styling.
   - **Notes PDF Download**: Simulates secure, scalable document downloads showing real-time loaders.
4. **Frosted Bottom Navigation & FAB**: Fully dynamic, glass-molded bottom taskbar hiding itself automatically on authentication screens to maximize space.
5. **Interactive Pomodoro Study Timer**: Floating top overlay widget offering 25-minute study intervals, countdown clocks, and custom start/pause physics.
6. **TikTok/Instagram-style Reels Feed**: Vertical full-screen swipe video reels featuring double-tap to like animations and quick-learning speed controllers (0.5x, 1x, 2x).
7. **Subject Study Communities**: Study forums with subject filters, announcement widgets, and interactive group-wide polling cards.
8. **Student-focused AI Study Buddy**: Gemini-inspired chat assistant loaded with flipping study flashcards, quiz generator templates, and conceptual explanations.
9. **Gamified Profiles**: Progression levels, daily XP counts, and earned badges grid.
10. **Direct Messaging Chats**: Interactive chat threads with active typing indicator simulations, mock attachments, and voice note player modules.

---

## 📂 Code & Package Architecture
The codebase conforms strictly to clean MVVM architecture standards:
```
com.instaeducate.app/
│
├── data/
│   ├── model/         # User, Post, Reel, Comment, ChatMessage, Community
│   └── repository/    # AuthRepository, PostRepository, CommunityRepository, ChatRepository, AIRepository
│
├── ui/
│   ├── theme/         # Color, Type, Theme (including AMOLED/Dark/Light Mode support)
│   ├── components/    # Reusable widgets (GlassmorphicCard, custom helper modifiers)
│   ├── screens/       # HomeScreen, ReelsScreen, UploadScreen, CommunitiesScreen, ProfileScreen, AIScreen, ChatScreen, AuthScreens
│   └── viewmodel/     # AuthViewModel, HomeViewModel, ReelsViewModel, CommunityViewModel, ChatViewModel, AIViewModel
│
├── navigation/        # Screen routes definitions and NavHost composable
└── MainActivity.kt    # Application launcher activity
```

---

## 💻 Importing Into Android Studio
1. Launch **Android Studio (Hedgehog or newer)**.
2. Click **File -> Open / Import** and select the `InstaEducate` folder.
3. Android Studio will automatically resolve Gradle dependencies based on `build.gradle.kts`.
4. Connect a physical Android device or launch an Emulator (SDK 34).
5. Click the **Run** button to launch the application.

---

## 🐙 Step-by-Step GitHub Publishing Guide
We have pre-configured a comprehensive `.gitignore` to keep caches, build files, and local property keys out of version control. Follow these simple shell steps to push this code directly to your GitHub repository:

1. **Initialize Git**:
   ```bash
   git init
   ```
2. **Add all files to staging**:
   ```bash
   git add .
   ```
3. **Commit the initial premium boilerplate**:
   ```bash
   git commit -m "feat: Initial premium InstaEducate Jetpack Compose codebase"
   ```
4. **Create a new, empty repository on GitHub**:
   - Go to [github.com/new](https://github.com/new).
   - Name it `InstaEducate` (keep it public or private as preferred).
   - Leave "Initialize with a README" **unchecked** since we already have one.

5. **Link and push to GitHub** (replace with your GitHub username):
   ```bash
   git branch -M main
   git remote add origin https://github.com/YOUR_USERNAME/InstaEducate.git
   git push -u origin main
   ```
