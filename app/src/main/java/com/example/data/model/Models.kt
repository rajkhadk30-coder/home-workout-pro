package com.example.data.model

enum class Language {
    ENGLISH,
    NEPALI
}

enum class ExerciseCategory(val displayNameEn: String, val displayNameNp: String) {
    FULL_BODY("Full Body", "पूर्ण शरीर"),
    CHEST("Chest", "छाती"),
    ARMS("Arms", "हात"),
    ABS("Abs", "पेट / एब्स"),
    LEGS("Legs", "खुट्टा"),
    BACK("Back", "पीठ"),
    SHOULDERS("Shoulders", "काँध"),
    CARDIO("Cardio", "कार्डियो"),
    HIIT("HIIT", "एच.आई.आई.टी (HIIT)"),
    STRETCHING("Stretching", "स्ट्रेचिङ"),
    WARM_UP("Warm Up", "वार्म अप")
}

enum class Difficulty(val labelEn: String, val labelNp: String) {
    BEGINNER("Beginner", "सुरुवाती"),
    INTERMEDIATE("Intermediate", "मध्यम"),
    ADVANCED("Advanced", "उच्च स्तर")
}

data class Exercise(
    val id: String,
    val nameEn: String,
    val nameNp: String,
    val descriptionEn: String,
    val descriptionNp: String,
    val category: ExerciseCategory,
    val targetMuscle: String,
    val difficulty: Difficulty,
    val caloriesBurned: Int,
    val durationSeconds: Int = 30,
    val repetitions: Int = 12,
    val restTimeSeconds: Int = 15,
    val voiceGuidanceEn: String,
    val voiceGuidanceNp: String,
    val animationType: String = "PUSHUP", // PUSHUP, SQUAT, PLANK, JUMPING_JACKS, LUNGE, CRUNCH, DIPS, BURPEE, MOUNTAIN_CLIMBER, HIGH_KNEES
    val videoUrl: String = "https://www.youtube.com/watch?v=iodq25D345c"
)

data class ChallengePlan(
    val id: String,
    val nameEn: String,
    val nameNp: String,
    val durationDays: Int,
    val level: Difficulty,
    val descriptionEn: String,
    val descriptionNp: String,
    val exerciseIds: List<String>
)

enum class DietCategory(val labelEn: String, val labelNp: String) {
    WEIGHT_LOSS("Weight Loss", "तौल घटाउने"),
    MUSCLE_GAIN("Muscle Gain", "मांसपेशी बढाउने"),
    VEGETARIAN("Vegetarian", "शाकाहारी"),
    HIGH_PROTEIN("High Protein", "उच्च प्रोटिन")
}

data class MealItem(
    val time: String,
    val nameEn: String,
    val nameNp: String,
    val calories: Int,
    val proteinGrams: Int,
    val descriptionEn: String,
    val descriptionNp: String
)

data class DietPlan(
    val id: String,
    val category: DietCategory,
    val titleEn: String,
    val titleNp: String,
    val descriptionEn: String,
    val descriptionNp: String,
    val dailyCalories: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatGrams: Int,
    val meals: List<MealItem>
)

data class AchievementBadge(
    val id: String,
    val titleEn: String,
    val titleNp: String,
    val descriptionEn: String,
    val descriptionNp: String,
    val iconName: String,
    val xpBonus: Int,
    val isUnlocked: Boolean = false
)
