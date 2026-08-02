package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class WorkoutRepository(private val dao: WorkoutDao? = null) {

    private fun requireDao(): WorkoutDao = dao ?: error("WorkoutDao is required for database operations")

    // 1. User Profile Operations
    val userProfile: Flow<UserProfileEntity> = dao?.getUserProfile()?.map {
        it ?: UserProfileEntity()
    } ?: kotlinx.coroutines.flow.flowOf(UserProfileEntity())

    suspend fun saveProfile(profile: UserProfileEntity) {
        requireDao().saveUserProfile(profile)
    }

    suspend fun updateLanguage(language: Language) {
        val current = requireDao().getUserProfileOnce() ?: UserProfileEntity()
        requireDao().saveUserProfile(current.copy(language = language.name))
    }

    suspend fun updateThemeMode(isDark: Boolean) {
        val current = requireDao().getUserProfileOnce() ?: UserProfileEntity()
        requireDao().saveUserProfile(current.copy(isDarkMode = isDark))
    }

    suspend fun updateStreakAndXp(earnedXp: Int) {
        val d = requireDao()
        val profile = d.getUserProfileOnce() ?: UserProfileEntity()
        val newXp = profile.xp + earnedXp
        val newLevel = (newXp / 200) + 1
        
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        val todayDay = calendar.get(Calendar.DAY_OF_YEAR)
        
        calendar.timeInMillis = profile.lastActiveTimestamp
        val lastDay = calendar.get(Calendar.DAY_OF_YEAR)
        
        val newStreak = when {
            todayDay == lastDay -> profile.currentStreak
            todayDay == lastDay + 1 || (lastDay > todayDay && todayDay == 1) -> profile.currentStreak + 1
            else -> 1
        }

        d.saveUserProfile(
            profile.copy(
                xp = newXp,
                level = newLevel,
                currentStreak = newStreak,
                lastActiveTimestamp = now
            )
        )
    }

    // 2. Workout Logs
    val workoutLogs: Flow<List<WorkoutLogEntity>> = dao?.getAllWorkoutLogs() ?: kotlinx.coroutines.flow.flowOf(emptyList())

    suspend fun recordWorkout(
        title: String,
        category: String,
        durationSeconds: Int,
        caloriesBurned: Int,
        exercisesCount: Int
    ) {
        val xpEarned = 100 + (caloriesBurned / 2)
        requireDao().insertWorkoutLog(
            WorkoutLogEntity(
                workoutTitle = title,
                category = category,
                durationSeconds = durationSeconds,
                caloriesBurned = caloriesBurned,
                exercisesCount = exercisesCount,
                xpEarned = xpEarned
            )
        )
        updateStreakAndXp(xpEarned)
    }

    // 3. Trackers
    fun getWaterLogsForToday(): Flow<List<WaterLogEntity>> {
        val dateToday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return dao?.getWaterLogsForDate(dateToday) ?: kotlinx.coroutines.flow.flowOf(emptyList())
    }

    suspend fun addWaterIntake(amountMl: Int) {
        val dateToday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        requireDao().insertWaterLog(
            WaterLogEntity(
                amountMl = amountMl,
                dateString = dateToday
            )
        )
    }

    suspend fun resetWaterIntake() {
        val dateToday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        requireDao().clearWaterLogsForDate(dateToday)
    }

    val weightLogs: Flow<List<WeightLogEntity>> = dao?.getAllWeightLogs() ?: kotlinx.coroutines.flow.flowOf(emptyList())

    suspend fun logWeight(weightKg: Float) {
        val d = requireDao()
        val dateToday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        d.insertWeightLog(
            WeightLogEntity(
                weightKg = weightKg,
                dateString = dateToday
            )
        )
        val profile = d.getUserProfileOnce() ?: UserProfileEntity()
        d.saveUserProfile(profile.copy(weightKg = weightKg))
    }

    val bodyMeasurements: Flow<List<BodyMeasurementEntity>> = dao?.getAllBodyMeasurements() ?: kotlinx.coroutines.flow.flowOf(emptyList())

    suspend fun logBodyMeasurement(chest: Float, waist: Float, biceps: Float, thighs: Float, hips: Float) {
        val dateToday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        requireDao().insertBodyMeasurement(
            BodyMeasurementEntity(
                chestCm = chest,
                waistCm = waist,
                bicepsCm = biceps,
                thighsCm = thighs,
                hipsCm = hips,
                dateString = dateToday
            )
        )
    }

    // 4. Custom Exercises (Admin / User Created)
    val customExercises: Flow<List<CustomExerciseEntity>> = dao?.getAllCustomExercises() ?: kotlinx.coroutines.flow.flowOf(emptyList())

    suspend fun addCustomExercise(
        nameEn: String,
        nameNp: String,
        category: String,
        targetMuscle: String,
        difficulty: String,
        calories: Int,
        duration: Int,
        reps: Int,
        videoUrl: String
    ) {
        requireDao().insertCustomExercise(
            CustomExerciseEntity(
                nameEn = nameEn,
                nameNp = nameNp,
                category = category,
                targetMuscle = targetMuscle,
                difficulty = difficulty,
                caloriesBurned = calories,
                durationSeconds = duration,
                repetitions = reps,
                videoUrl = videoUrl
            )
        )
    }

    suspend fun deleteCustomExercise(id: Long) {
        requireDao().deleteCustomExercise(id)
    }

    // 5. Static Challenge Plans
    val challengesList: List<ChallengePlan> = listOf(
        ChallengePlan(
            id = "c_7",
            nameEn = "7-Day Jumpstart",
            nameNp = "७-दिने जम्पस्टार्ट चुनौती",
            durationDays = 7,
            level = Difficulty.BEGINNER,
            descriptionEn = "Kickstart your home fitness routine with quick 10-minute daily bodyweight workouts.",
            descriptionNp = "दैनिक १० मिनेटको सरल व्यायामबाट आफ्नो फिटनेस यात्रा सुरु गर्नुहोस्।",
            exerciseIds = listOf("fb_1", "chest_1", "abs_1", "leg_1", "cardio_1", "stretch_1")
        ),
        ChallengePlan(
            id = "c_14",
            nameEn = "14-Day Fat Burner",
            nameNp = "१४-दिने बोसो घटाउने चुनौती",
            durationDays = 14,
            level = Difficulty.INTERMEDIATE,
            descriptionEn = "High intensity cardio and abs combinations designed to shed excess weight fast.",
            descriptionNp = "छिटो तौल घटाउन उच्च गतिको कार्डियो र एब्स कम्बिनेसन।",
            exerciseIds = listOf("fb_2", "hiit_1", "abs_3", "abs_5", "cardio_3", "leg_4")
        ),
        ChallengePlan(
            id = "c_30",
            nameEn = "30-Day Full Body Shred",
            nameNp = "३०-दिने पूर्ण शरीर रुपान्तरण",
            durationDays = 30,
            level = Difficulty.INTERMEDIATE,
            descriptionEn = "Complete 30-day transformation plan targeting chest, arms, core, and legs.",
            descriptionNp = "छाती, हात, पेट र खुट्टाको पूर्ण विकासका लागि ३० दिने कार्यक्रम।",
            exerciseIds = listOf("chest_2", "arm_1", "abs_2", "leg_2", "back_1", "shoulder_1", "hiit_2")
        ),
        ChallengePlan(
            id = "c_60",
            nameEn = "60-Day Beast Sculpting",
            nameNp = "६०-दिने बिस्ट मसल बिल्डर",
            durationDays = 60,
            level = Difficulty.ADVANCED,
            descriptionEn = "Build serious lean muscle volume and explosive power with advanced progressions.",
            descriptionNp = "मांसपेशीको आकार र शक्ति बढाउन ६० दिनको उच्च स्तरको तालिम।",
            exerciseIds = listOf("chest_3", "chest_5", "arm_2", "abs_9", "leg_8", "hiit_3", "back_3")
        ),
        ChallengePlan(
            id = "c_90",
            nameEn = "90-Day Master Challenge",
            nameNp = "९०-दिने मास्टर च्यालेन्ज",
            durationDays = 90,
            level = Difficulty.ADVANCED,
            descriptionEn = "The ultimate 3-month home workout mastery program for elite conditioning.",
            descriptionNp = "३ महिनामा शरीरलाई पूर्ण रूपमा एथलेटिक र फिट बनाउने मास्टर प्लान।",
            exerciseIds = listOf("hiit_1", "hiit_2", "hiit_3", "chest_4", "arm_3", "abs_10", "leg_11", "back_4")
        )
    )

    // 6. Diet Plans
    val dietPlansList: List<DietPlan> = listOf(
        DietPlan(
            id = "d_weight_loss",
            category = DietCategory.WEIGHT_LOSS,
            titleEn = "Weight Loss & Fat Shred",
            titleNp = "तौल र बोसो घटाउने आहार",
            descriptionEn = "Calorie deficit diet focusing on lean protein, high fiber, and antioxidant greens.",
            descriptionNp = "कम क्यालोरी, धेरै प्रोटिन र फाइबरयुक्त खाना जसले छिटो तौल घटाउन मद्दत गर्छ।",
            dailyCalories = 1800,
            proteinGrams = 130,
            carbsGrams = 160,
            fatGrams = 45,
            meals = listOf(
                MealItem("Breakfast", "Oatmeal with Berries & Almonds", "ओट्स र ओखर/बदाम", 350, 15, "Complex carbs for long energy", "दिनभर ऊर्जा दिने जटिल कार्बोहाइड्रेट"),
                MealItem("Lunch", "Grilled Chicken Salad with Olive Oil", "ग्रिल्ड चिकेन र हरियो सागसब्जी", 500, 42, "High protein lean meal", "उच्च प्रोटिनयुक्त पोषिलो खाना"),
                MealItem("Snack", "Greek Yogurt & Green Tea", "दही र ग्रिन टी", 180, 18, "Metabolism booster", "पाचन र मेटाबोलिज्म बढाउने"),
                MealItem("Dinner", "Baked Fish / Tofu with Steamed Vegetables", "माछा वा तोफु र उसिनेको तरकारी", 450, 38, "Light night dinner", "रातिको हल्का र स्वास्थ्यवर्धक खाना")
            )
        ),
        DietPlan(
            id = "d_muscle_gain",
            category = DietCategory.MUSCLE_GAIN,
            titleEn = "Hypertrophy Muscle Gain",
            titleNp = "मांसपेशी बढाउने उच्च ऊर्जा आहार",
            descriptionEn = "Calorie surplus meal plan packed with clean carbs, healthy fats, and 2g protein per kg.",
            descriptionNp = "मांसपेशीको आकार बढाउन आवश्यक प्रोटिन, क्यालोरी र पोषक तत्व भएको योजना।",
            dailyCalories = 2800,
            proteinGrams = 175,
            carbsGrams = 320,
            fatGrams = 70,
            meals = listOf(
                MealItem("Breakfast", "4 Eggs, Whole Wheat Toast, Avocado", "४ वटा अण्डा, गहुँको पाउरोटी र एभोकाडो", 650, 32, "Power breakfast", "शक्तिशाली बिहानको खाजा"),
                MealItem("Lunch", "Chicken Breast, Brown Rice, Broccoli", "चिकेन ब्रेस्ट, ब्राउन राइस र ब्रोकाउली", 800, 55, "Main muscle building lunch", "मांसपेशी निर्माण गर्ने मुख्य खाना"),
                MealItem("Snack", "Peanut Butter Banana Smoothie with Whey", "पिनट बटर, केरा र दुधको स्मूदी", 550, 30, "Post workout anabolic shake", "व्यायाम पछि पिउने ऊर्जादायक पेय"),
                MealItem("Dinner", "Lean Beef or Cottage Cheese Paneer Curry with Quinoa", "पनीर/मासु र क्विनोआ/दाल", 700, 48, "Sustained overnight recovery", "रातिको मांसपेशी पुनःप्राप्ति खाना")
            )
        ),
        DietPlan(
            id = "d_vegetarian",
            category = DietCategory.VEGETARIAN,
            titleEn = "Pure Vegetarian Fitness Diet",
            titleNp = "शुद्ध शाकाहारी फिटनेस आहार",
            descriptionEn = "Plant-powered nutrition loaded with Paneer, Lentils, Chickpeas, Tofu, and Nuts.",
            descriptionNp = "पनीर, गेडागुडी, चना, तोफु र बदामयुक्त पूर्ण शाकाहारी पोषिलो आहार।",
            dailyCalories = 2200,
            proteinGrams = 120,
            carbsGrams = 250,
            fatGrams = 60,
            meals = listOf(
                MealItem("Breakfast", "Sprouted Chickpeas (Chana) & Almond Milk Shake", "उम्रेको चना र बदाम दुध", 450, 22, "Natural plant protein boost", "प्राकृतिक वनस्पति प्रोटिन"),
                MealItem("Lunch", "Dal, Cottage Cheese Paneer, Brown Rice, Salad", "दाल, पनीर तरकारी, ब्राउन राइस र सलाद", 650, 35, "Traditional balanced meal", "सन्तुलित नेपाली शाकाहारी खाना"),
                MealItem("Snack", "Roasted Makhana & Roasted Soybeans", "भाटेको मखाना र भटमास", 250, 18, "Crunchy high protein snack", "खाजाको रूपमा भटमास र मखाना"),
                MealItem("Dinner", "Soya Chunks & Mixed Vegetable Curry with Roti", "सोयाबिन र तरकारीको झोल र रोटी", 500, 38, "Rich in amino acids", "अमीनो एसिडले भरपूर खाना")
            )
        ),
        DietPlan(
            id = "d_high_protein",
            category = DietCategory.HIGH_PROTEIN,
            titleEn = "Ultra High Protein Power",
            titleNp = "अल्ट्रा हाई प्रोटिन पावर",
            descriptionEn = "Accelerate recovery and shred fat with 40% protein macronutrient distribution.",
            descriptionNp = "मांसपेशी छिटो मर्मत गर्न र बोसो पगाल्न ४०% प्रोटिनयुक्त विशेष आहार।",
            dailyCalories = 2400,
            proteinGrams = 200,
            carbsGrams = 180,
            fatGrams = 55,
            meals = listOf(
                MealItem("Breakfast", "Egg White Scramble with Spinach & Mushrooms", "अण्डाको सेतो भाग र पालुङ्गो", 400, 38, "Pure protein start", "शुद्ध प्रोटिनको सुरुवात"),
                MealItem("Lunch", "Turkey or Chicken Breast Bowl with Black Beans", "चिकेन र कालो सिमीको कचौरा", 700, 60, "Protein packed lunch", "प्रोटिनले भरिपूर्ण दिउँसोको खाना"),
                MealItem("Snack", "Cottage Cheese with Walnuts", "पनीर र ओखर", 300, 28, "Casein slow digesting protein", "बिस्तारै पच्ने क्यासिन प्रोटिन"),
                MealItem("Dinner", "Salmon or Tofu Steak with Asparagus", "सामन माछा वा तोफु र तरकारी", 600, 52, "Omega 3 rich dinner", "ओमेगा ३ युक्त स्वास्थ्यवर्धक खाना")
            )
        )
    )

    // 7. Achievement Badges
    fun getAchievementsList(userXp: Int, userStreak: Int, workoutCount: Int): List<AchievementBadge> {
        return listOf(
            AchievementBadge(
                id = "ach_first",
                titleEn = "First Step",
                titleNp = "पहिलो पाइला",
                descriptionEn = "Complete your 1st workout",
                descriptionNp = "आफ्नो पहिलो व्यायाम पूरा गर्नुहोस्",
                iconName = "FITNESS_CENTER",
                xpBonus = 50,
                isUnlocked = workoutCount >= 1
            ),
            AchievementBadge(
                id = "ach_streak_3",
                titleEn = "3-Day Warrior",
                titleNp = "३-दिने योद्धा",
                descriptionEn = "Maintain a 3-day workout streak",
                descriptionNp = "३ दिन लगातार व्यायाम गर्नुहोस्",
                iconName = "LOCAL_FIRE_DEPARTMENT",
                xpBonus = 100,
                isUnlocked = userStreak >= 3
            ),
            AchievementBadge(
                id = "ach_streak_7",
                titleEn = "7-Day Iron Will",
                titleNp = "७-दिने कडा संकल्प",
                descriptionEn = "Maintain a 7-day workout streak",
                descriptionNp = "७ दिन लगातार व्यायाम गर्नुहोस्",
                iconName = "WHATSHOT",
                xpBonus = 250,
                isUnlocked = userStreak >= 7
            ),
            AchievementBadge(
                id = "ach_xp_500",
                titleEn = "XP Rising Star",
                titleNp = "XP उदीयमान तारा",
                descriptionEn = "Reach 500 XP total",
                descriptionNp = "कुल ५०० XP प्राप्त गर्नुहोस्",
                iconName = "STAR",
                xpBonus = 150,
                isUnlocked = userXp >= 500
            ),
            AchievementBadge(
                id = "ach_xp_1500",
                titleEn = "Fitness Master",
                titleNp = "फिटनेस मास्टर",
                descriptionEn = "Reach 1500 XP total",
                descriptionNp = "कुल १५०० XP प्राप्त गर्नुहोस्",
                iconName = "MILITARY_TECH",
                xpBonus = 500,
                isUnlocked = userXp >= 1500
            ),
            AchievementBadge(
                id = "ach_workouts_10",
                titleEn = "Century Club",
                titleNp = "१० वर्कआउट क्लब",
                descriptionEn = "Complete 10 total workout sessions",
                descriptionNp = "कुल १० वटा व्यायाम सेसन पूरा गर्नुहोस्",
                iconName = "EMOJI_EVENTS",
                xpBonus = 300,
                isUnlocked = workoutCount >= 10
            )
        )
    }
}
