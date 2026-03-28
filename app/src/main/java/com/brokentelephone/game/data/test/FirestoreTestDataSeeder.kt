package com.brokentelephone.game.data.test

import com.brokentelephone.game.data.mapper.toMap
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.features.choose_username.model.SuggestedUsernames
import com.brokentelephone.game.features.edit_avatar.model.Avatars
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreTestDataSeeder(
    private val firestore: FirebaseFirestore,
) {
    private val postsCollection get() = firestore.collection("posts")
    private val usersCollection get() = firestore.collection("users")

    suspend fun seedUsers() {
        FAKE_AUTHORS.forEach { (id, username) ->
            val now = System.currentTimeMillis()
            val user = User(
                id = id,
                username = username,
                email = "$username@test.com".lowercase(),
                avatarUrl = Avatars.all.random().url,
                authProvider = AuthProvider.EMAIL,
                createdAt = now,
                updatedAt = now,
                notifications = NotificationType.entries,
                onboardingStep = OnboardingStep.COMPLETED,
            )
            usersCollection.document(id).set(user.toMap()).await()
        }
    }

    suspend fun seedFriends(targetUserId: String = "Va9OfTygaXOH3OLRWPMjOs4TR5y2", count: Int = 100) {
        val now = System.currentTimeMillis()
        val friendIds = mutableListOf<String>()

        repeat(count) {
            val docRef = usersCollection.document()
            val username = SuggestedUsernames.random()
            val user = User(
                id = docRef.id,
                username = username,
                email = "${username.replace(" ", "").lowercase()}_${it}@test.com",
                avatarUrl = Avatars.all.random().url,
                authProvider = AuthProvider.EMAIL,
                createdAt = now,
                updatedAt = now,
                notifications = NotificationType.entries,
                onboardingStep = OnboardingStep.COMPLETED,
                friendIds = listOf(targetUserId),
            )
            docRef.set(user.toMap()).await()
            friendIds.add(docRef.id)
        }

        usersCollection.document(targetUserId)
            .update("friendIds", FieldValue.arrayUnion(*friendIds.toTypedArray()))
            .await()
    }

    suspend fun seedPosts(count: Int = 100) {
        repeat(count) {
            val docRef = postsCollection.document()
            val now = System.currentTimeMillis() - (0..30L * 24 * 60 * 60 * 1000).random()
            val author = FAKE_AUTHORS.random()
            val text = FAKE_TEXTS.random()
            val avatarUrl = Avatars.all.random().url

            val post = Post(
                id = docRef.id,
                chainId = docRef.id,
                authorId = author.first,
                authorName = author.second,
                avatarUrl = avatarUrl,
                content = PostContent.Text(text = text),
                createdAt = now,
                updatedAt = now,
                status = PostStatus.AVAILABLE,
                sessionId = null,
                generation = 1,
                maxGenerations = (3..6).random(),
                textTimeLimit = 120,
                drawingTimeLimit = 180,
            )
            val chainEntryRef = docRef.collection("chain").document()
//            val chainEntry = post.copy(id = chainEntryRef.id, parentId = docRef.id)
//            val userPostRef = usersCollection
//                .document(author.first)
//                .collection("posts")
//                .document(docRef.id)

            firestore.runBatch { batch ->
                batch.set(docRef, post.toMap())
//                batch.set(chainEntryRef, chainEntry.toMap())
//                batch.set(userPostRef, post.toMap())
            }.await()
        }
    }

    private companion object {
        val FAKE_AUTHORS = listOf(
            "user_001" to "Alex",
            "user_002" to "Maria",
            "user_003" to "John",
            "user_004" to "Sofia",
            "user_005" to "Dmytro",
        )

        val FAKE_TEXTS = listOf(
            "A cat is sitting on a windowsill watching birds outside",
            "Two astronauts playing chess on the Moon",
            "A dragon learning to bake croissants",
            "The sun is setting over a quiet mountain lake",
            "A robot teaching a dog how to fetch",
            "An old lighthouse keeper talking to a whale",
            "Three penguins trying to hail a taxi in New York",
            "A wizard forgot where he put his wand",
            "Kids building a snowman taller than their house",
            "A pirate ship caught in a traffic jam",
            "A grandmother teaching her dog to play piano",
            "Two clouds arguing over who is fluffier",
            "A chef preparing spaghetti for a hundred ghosts",
            "An elephant trying to fit into a tiny red car",
            "A mermaid applying for a job at an aquarium",
            "A bear opening a honey-themed coffee shop",
            "A giraffe struggling to use an ATM",
            "Space cowboys herding asteroids",
            "A detective interrogating a suspicious houseplant",
            "Time traveler stuck in a medieval bakery",
        )
    }
}
