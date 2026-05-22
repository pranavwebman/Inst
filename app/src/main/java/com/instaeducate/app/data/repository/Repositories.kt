package com.instaeducate.app.data.repository

import com.instaeducate.app.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: Flow<User?> = _currentUser

    suspend fun login(email: String, password: String): Result<User> {
        delay(1000) // Simulated network lag
        if (!email.contains("@")) return Result.failure(Exception("Invalid email format"))
        
        val user = User(
            uid = UUID.randomUUID().toString(),
            username = email.substringBefore("@").replaceFirstChar { it.uppercase() },
            email = email,
            role = UserRole.STUDENT,
            bio = "Lifelong learner. Scroll. Learn. Grow.",
            educationLevel = "College Sophomore",
            subjectsOfInterest = listOf("Coding", "Math", "AI"),
            followersCount = 142,
            followingCount = 98,
            learningStreak = 12,
            xpPoints = 1240,
            achievementBadges = listOf("streak_10", "quiz_champ")
        )
        _currentUser.value = user
        return Result.success(user)
    }

    suspend fun signup(email: String, username: String): Result<User> {
        delay(1000)
        val user = User(
            uid = UUID.randomUUID().toString(),
            username = username,
            email = email,
            role = UserRole.STUDENT,
            bio = "Ready to learn on InstaEducate!",
            learningStreak = 1,
            xpPoints = 100
        )
        _currentUser.value = user
        return Result.success(user)
    }

    suspend fun updateRole(role: UserRole, education: String, subjects: List<String>) {
        _currentUser.value = _currentUser.value?.copy(
            role = role,
            educationLevel = education,
            subjectsOfInterest = subjects
        )
    }

    fun logout() {
        _currentUser.value = null
    }
}

class PostRepository {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: Flow<List<Post>> = _posts

    private val _reels = MutableStateFlow<List<Reel>>(emptyList())
    val reels: Flow<List<Reel>> = _reels

    init {
        // Initialize Feed with premium educational content
        _posts.value = listOf(
            Post(
                authorName = "Prof. Andrew",
                authorRole = UserRole.TEACHER,
                type = PostType.QUIZ_CARD,
                title = "Daily Machine Learning Challenge",
                caption = "What does the 'learning rate' parameter in gradient descent optimize? Let's check your understanding in the quiz card below!",
                subjectTag = "AI",
                quizCard = QuizCard(
                    question = "What happens if the learning rate is too high?",
                    options = listOf(
                        "Gradient descent converges very slowly",
                        "The loss oscillates and might diverge",
                        "The model overfits the training set instantly",
                        "It makes the feature scaling redundant"
                    ),
                    correctIndex = 1,
                    explanation = "A high learning rate can cause the weights to overshoot the optimal minimum, leading to oscillation or divergence of the cost function."
                )
            ),
            Post(
                authorName = "CodeBytes",
                authorRole = UserRole.CREATOR,
                type = PostType.CODING_SNIPPET,
                title = "Kotlin Scope Functions Explained",
                caption = "Scope functions like 'let', 'run', 'with', 'apply', and 'also' make your code concise. Here is a cheat sheet snippet!",
                subjectTag = "Coding",
                codeLanguage = "kotlin",
                codeSnippet = """
// Use 'apply' to initialize object properties
val student = Student().apply {
    name = "Elena"
    streak = 15
    xp = 1200
}

// Use 'let' for safe null-checks
student?.let {
    println("Active streak: ${'$'}{it.streak} days!")
}
                """.trimIndent()
            ),
            Post(
                authorName = "Dr. Sarah Miller",
                authorRole = UserRole.TEACHER,
                type = PostType.NOTES_PDF,
                title = "Calculus II: Derivatives & Integrals PDF",
                caption = "Full study syllabus notes for engineering student midterms. Download the document below to save offline.",
                subjectTag = "Math",
                notesPdfSize = "2.4 MB",
                notesPdfUrl = "https://example.com/calculus2.pdf"
            )
        )

        // Initialize Reels
        _reels.value = listOf(
            Reel(
                authorName = "DevByte AI",
                videoUrl = "https://sample.com/video1.mp4",
                caption = "How LLMs construct vector embeddings using high-dimensional space! 🤖",
                subjectTag = "AI",
                hashtags = listOf("ArtificialIntelligence", "Embeddings", "EdTok"),
                likesCount = 2540,
                isLikedByMe = true
            ),
            Reel(
                authorName = "ScienceWonders",
                videoUrl = "https://sample.com/video2.mp4",
                caption = "Why the sky looks blue due to Rayleigh scattering of light! ☀️🌈",
                subjectTag = "Science",
                hashtags = listOf("Physics", "ScienceRules", "Nature"),
                likesCount = 1980
            ),
            Reel(
                authorName = "HackerCave",
                videoUrl = "https://sample.com/video3.mp4",
                caption = "Understanding Cross-Site Scripting (XSS) in under 60 seconds! 🛡️💻",
                subjectTag = "Cybersecurity",
                hashtags = listOf("Cybersecurity", "BugBounty", "EthicalHacking"),
                likesCount = 4210
            )
        )
    }

    suspend fun toggleLike(postId: String) {
        _posts.value = _posts.value.map {
            if (it.id == postId) {
                it.copy(
                    isLikedByMe = !it.isLikedByMe,
                    likesCount = if (it.isLikedByMe) it.likesCount - 1 else it.likesCount + 1
                )
            } else it
        }
    }

    suspend fun toggleSave(postId: String) {
        _posts.value = _posts.value.map {
            if (it.id == postId) {
                it.copy(isSavedByMe = !it.isSavedByMe)
            } else it
        }
    }

    suspend fun submitPost(post: Post) {
        _posts.value = listOf(post) + _posts.value
    }
}

class CommunityRepository {
    private val _communities = MutableStateFlow<List<Community>>(emptyList())
    val communities: Flow<List<Community>> = _communities

    init {
        _communities.value = listOf(
            Community(
                name = "Python Programming",
                description = "For beginners and pros looking to learn Python, Django, FastAPI and Machine Learning.",
                memberCount = 1420,
                subjectCategory = "Coding",
                activePoll = Poll(
                    question = "Which framework do you prefer for building Python web APIs?",
                    options = listOf("FastAPI", "Flask", "Django"),
                    votes = listOf(142, 68, 95)
                ),
                discussionThreads = listOf(
                    DiscussionThread(authorName = "Alex", content = "Does anyone know the best way to manage dependencies? Poetry or pipenv?"),
                    DiscussionThread(authorName = "Meera", content = "Poetry is amazing, it handles virtualenvs and locks versioning cleanly!")
                )
            ),
            Community(
                name = "JEE Mathematics Preparation",
                description = "Ace the competitive joint engineering entrance exams with shortcuts and study guides.",
                memberCount = 850,
                subjectCategory = "Math",
                activePoll = Poll(
                    question = "What is your weakest topic in JEE Math?",
                    options = listOf("Coordinate Geometry", "Calculus", "Probability"),
                    votes = listOf(72, 114, 45)
                ),
                discussionThreads = listOf(
                    DiscussionThread(authorName = "Rohan", content = "Let's share shortcuts for fast indefinite integration formulas here.")
                )
            )
        )
    }

    suspend fun voteInPoll(communityId: String, selectedIndex: Int) {
        _communities.value = _communities.value.map {
            if (it.id == communityId && it.activePoll != null) {
                val updatedVotes = it.activePoll.votes.toMutableList()
                updatedVotes[selectedIndex] = updatedVotes[selectedIndex] + 1
                it.copy(
                    activePoll = it.activePoll.copy(
                        votes = updatedVotes,
                        userVotedIndex = selectedIndex
                    )
                )
            } else it
        }
    }

    suspend fun postToThread(communityId: String, content: String, author: String) {
        _communities.value = _communities.value.map {
            if (it.id == communityId) {
                it.copy(
                    discussionThreads = it.discussionThreads + DiscussionThread(
                        authorName = author,
                        content = content
                    )
                )
            } else it
        }
    }
}

class ChatRepository {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: Flow<List<ChatMessage>> = _messages

    init {
        _messages.value = listOf(
            ChatMessage(senderName = "Teacher Sarah", messageText = "Hi! I saw you bookmarked the Calculus II integrals PDF.", isRead = true),
            ChatMessage(senderName = "Teacher Sarah", messageText = "If you have any doubts on the fundamental theorem of calculus, feel free to ask here!", isRead = true)
        )
    }

    suspend fun sendMessage(text: String, voiceDuration: String = "") {
        val msg = ChatMessage(
            senderId = "me",
            senderName = "Me",
            messageText = text,
            voiceNoteDuration = voiceDuration
        )
        _messages.value = _messages.value + msg
    }
}

class AIRepository {
    suspend fun getAIResponse(prompt: String): String {
        delay(1200) // Simulated intelligence reasoning delay
        val cleanPrompt = prompt.lowercase()
        return when {
            cleanPrompt.contains("summarize") -> {
                "📚 **AI Concept Summary:**\n\n1. **Core Subject**: Direct and indirect neural connections.\n2. **Summary**: Information paths rely on deep semantic encoding. High frequencies strengthen storage pathways, meaning active recall speeds integration by 35%!\n\n💡 *Study Tip: Explain this concept out loud to solidify the neural hook.*"
            }
            cleanPrompt.contains("quiz") || cleanPrompt.contains("generate") -> {
                "📝 **AI Mini-Quiz Created!**\n\n*Question:* Which sorting algorithm guarantees O(N log N) worst-case time complexity?\n\n1. Quick Sort\n2. Bubble Sort\n3. Merge Sort\n4. Selection Sort\n\n*Answer is Merge Sort because it splits recursively and merges in O(N).*"
            }
            cleanPrompt.contains("timer") || cleanPrompt.contains("pomodoro") -> {
                "⏱️ **Pomodoro Guidance:**\n\nStudies show that standard **25-minute study / 5-minute break** blocks prevent visual fatigue and optimize attention spans. Start the study timer overlay from your feed screen!"
            }
            else -> {
                "Hello fellow student! 🎓 I am your **InstaEducate Study Assistant**.\n\nI can help you:\n• Explain complex concepts (e.g. 'Explain derivatives')\n• Summarize your notes ('Summarize neural pathways')\n• Create mock quizzes ('Generate a coding quiz')\n• Create flashcards and study schedules.\n\nTell me what you are working on today!"
            }
        }
    }
}
