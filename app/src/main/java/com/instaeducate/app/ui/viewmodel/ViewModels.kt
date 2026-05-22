package com.instaeducate.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instaeducate.app.data.model.*
import com.instaeducate.app.data.repository.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.currentUser.collect {
                _currentUser.value = it
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repository.login(email, password)
                .onSuccess {
                    onSuccess()
                }
                .onFailure {
                    _errorMessage.value = it.message ?: "Authentication failed"
                }
            _isLoading.value = false
        }
    }

    fun signup(email: String, username: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repository.signup(email, username)
                .onSuccess {
                    onSuccess()
                }
                .onFailure {
                    _errorMessage.value = it.message ?: "Registration failed"
                }
            _isLoading.value = false
        }
    }

    fun completeOnboarding(role: UserRole, education: String, subjects: List<String>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.updateRole(role, education, subjects)
            onSuccess()
        }
    }

    fun logout(onSuccess: () -> Unit) {
        repository.logout()
        onSuccess()
    }
}

class HomeViewModel(private val repository: PostRepository) : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _downloadProgress = MutableStateFlow<Map<String, Int>>(emptyMap())
    val downloadProgress: StateFlow<Map<String, Int>> = _downloadProgress.asStateFlow()

    init {
        viewModelScope.launch {
            repository.posts.collect {
                _posts.value = it
            }
        }
    }

    fun toggleLike(postId: String) {
        viewModelScope.launch {
            repository.toggleLike(postId)
        }
    }

    fun toggleSave(postId: String) {
        viewModelScope.launch {
            repository.toggleSave(postId)
        }
    }

    fun simulateNotesDownload(postId: String) {
        viewModelScope.launch {
            for (i in 0..100 step 20) {
                _downloadProgress.value = _downloadProgress.value.toMutableMap().apply {
                    put(postId, i)
                }
                delay(300)
            }
            // Clear progress after download completes to return to default state
            delay(1000)
            _downloadProgress.value = _downloadProgress.value.toMutableMap().apply {
                remove(postId)
            }
        }
    }

    fun addPost(title: String, caption: String, category: String, type: PostType, code: String = "") {
        viewModelScope.launch {
            val post = Post(
                authorName = "Me",
                authorRole = UserRole.STUDENT,
                type = type,
                title = title,
                caption = caption,
                subjectTag = category,
                codeSnippet = code,
                codeLanguage = if (code.isNotEmpty()) "kotlin" else ""
            )
            repository.submitPost(post)
        }
    }
}

class ReelsViewModel(private val repository: PostRepository) : ViewModel() {
    private val _reels = MutableStateFlow<List<Reel>>(emptyList())
    val reels: StateFlow<List<Reel>> = _reels.asStateFlow()

    init {
        viewModelScope.launch {
            repository.reels.collect {
                _reels.value = it
            }
        }
    }

    fun toggleLikeReel(reelId: String) {
        _reels.value = _reels.value.map {
            if (it.id == reelId) {
                it.copy(
                    isLikedByMe = !it.isLikedByMe,
                    likesCount = if (it.isLikedByMe) it.likesCount - 1 else it.likesCount + 1
                )
            } else it
        }
    }
}

class CommunityViewModel(private val repository: CommunityRepository) : ViewModel() {
    private val _communities = MutableStateFlow<List<Community>>(emptyList())
    val communities: StateFlow<List<Community>> = _communities.asStateFlow()

    init {
        viewModelScope.launch {
            repository.communities.collect {
                _communities.value = it
            }
        }
    }

    fun voteInPoll(communityId: String, optionIndex: Int) {
        viewModelScope.launch {
            repository.voteInPoll(communityId, optionIndex)
        }
    }

    fun addCommentToThread(communityId: String, content: String) {
        viewModelScope.launch {
            if (content.trim().isNotEmpty()) {
                repository.postToThread(communityId, content, "Me")
            }
        }
    }
}

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    init {
        viewModelScope.launch {
            repository.messages.collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            if (text.trim().isNotEmpty()) {
                repository.sendMessage(text)
                
                // Simulate active response from teacher after a small delay
                _isTyping.value = true
                delay(2000)
                _isTyping.value = false
                repository.sendMessage("Thanks for your question! I'm reviewing your solution now and will reply with dynamic feedback in our study group.")
            }
        }
    }

    fun sendVoiceNote(duration: String) {
        viewModelScope.launch {
            repository.sendMessage("🎤 Voice note sent", voiceDuration = duration)
        }
    }
}

class AIViewModel(private val repository: AIRepository) : ViewModel() {
    private val _aiMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val aiMessages: StateFlow<List<ChatMessage>> = _aiMessages.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    init {
        _aiMessages.value = listOf(
            ChatMessage(
                senderId = "ai",
                senderName = "InstaEducate Assistant",
                messageText = "Hello! I am your AI learning assistant. Ask me to:\n• *Summarize notes*\n• *Generate a coding quiz*\n• *Explain a complex concept*"
            )
        )
    }

    fun askAI(question: String) {
        viewModelScope.launch {
            if (question.trim().isEmpty()) return@launch
            
            // Add user message to thread
            val userMsg = ChatMessage(senderId = "user", senderName = "Me", messageText = question)
            _aiMessages.value = _aiMessages.value + userMsg
            
            _isGenerating.value = true
            val response = repository.getAIResponse(question)
            _isGenerating.value = false
            
            val aiMsg = ChatMessage(senderId = "ai", senderName = "InstaEducate Assistant", messageText = response)
            _aiMessages.value = _aiMessages.value + aiMsg
        }
    }
}
