package com.instaeducate.app.data.model

import java.util.UUID

enum class UserRole {
    STUDENT, TEACHER, CREATOR
}

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val role: UserRole = UserRole.STUDENT,
    val profilePhotoUrl: String = "",
    val bio: String = "",
    val educationLevel: String = "", // e.g. "College Student", "High Schooler"
    val subjectsOfInterest: List<String> = emptyList(),
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val learningStreak: Int = 0,
    val xpPoints: Int = 0,
    val achievementBadges: List<String> = emptyList() // List of badge IDs
)

enum class PostType {
    NOTES_PDF, IMAGE, CAROUSEL, CODING_SNIPPET, QUIZ_CARD
}

data class Post(
    val id: String = UUID.randomUUID().toString(),
    val authorId: String = "",
    val authorName: String = "",
    val authorAvatarUrl: String = "",
    val authorRole: UserRole = UserRole.STUDENT,
    val type: PostType = PostType.IMAGE,
    val title: String = "",
    val caption: String = "",
    val subjectTag: String = "",
    val mediaUrls: List<String> = emptyList(),
    val notesPdfUrl: String = "", // Optional PDF note download link
    val notesPdfSize: String = "",
    val codeSnippet: String = "",
    val codeLanguage: String = "",
    val quizCard: QuizCard? = null,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val isSavedByMe: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class QuizCard(
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0,
    val selectedIndex: Int? = null, // Track local user answer selection
    val explanation: String = ""
)

data class Reel(
    val id: String = UUID.randomUUID().toString(),
    val authorId: String = "",
    val authorName: String = "",
    val authorAvatarUrl: String = "",
    val videoUrl: String = "",
    val caption: String = "",
    val subjectTag: String = "",
    val hashtags: List<String> = emptyList(),
    val likesCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class Comment(
    val id: String = UUID.randomUUID().toString(),
    val postId: String = "",
    val authorName: String = "",
    val authorAvatarUrl: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class Community(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val memberCount: Int = 0,
    val subjectCategory: String = "",
    val iconUrl: String = "",
    val activePoll: Poll? = null,
    val discussionThreads: List<DiscussionThread> = emptyList()
)

data class Poll(
    val question: String = "",
    val options: List<String> = emptyList(),
    val votes: List<Int> = emptyList(),
    val userVotedIndex: Int? = null
)

data class DiscussionThread(
    val id: String = UUID.randomUUID().toString(),
    val authorName: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val senderId: String = "",
    val senderName: String = "",
    val messageText: String = "",
    val voiceNoteDuration: String = "", // Optional voice note metadata
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class AchievementBadge(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val iconResource: String = "",
    val xpRequired: Int = 0
)
