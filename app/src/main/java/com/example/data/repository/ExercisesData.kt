package com.example.data.repository

import com.example.data.model.Exercise
import com.example.data.model.WorkoutPlan

object ExercisesData {
    val samplePlans = listOf(
        WorkoutPlan(
            id = "abs_beginner",
            title = "Abs Beginner",
            category = "Abs",
            level = "Beginner",
            durationMinutes = 10,
            totalCalories = 85,
            exerciseCount = 5,
            exercises = listOf(
                Exercise("e1", "Jumping Jacks", "Cardio", 30, 0, 15, "Jump spreading legs and bringing hands overhead.", "jumping_jacks"),
                Exercise("e2", "Abdominal Crunches", "Abs", 0, 16, 20, "Lie on back, bend knees, curl shoulders towards hips.", "crunches"),
                Exercise("e3", "Plank Hold", "Abs", 40, 0, 18, "Hold forearms and toes on floor keeping body straight.", "plank"),
                Exercise("e4", "Russian Twists", "Abs", 0, 20, 15, "Sit, lean back slightly, twist torso side to side.", "twists"),
                Exercise("e5", "Cobra Stretch", "Stretch", 30, 0, 10, "Lie face down, lift chest stretching abs.", "cobra")
            )
        ),
        WorkoutPlan(
            id = "chest_intermediate",
            title = "Chest & Core Blast",
            category = "Chest",
            level = "Intermediate",
            durationMinutes = 15,
            totalCalories = 140,
            exerciseCount = 6,
            exercises = listOf(
                Exercise("e6", "Standard Push-Ups", "Chest", 0, 15, 25, "Classic push-ups engaging chest and triceps.", "pushup"),
                Exercise("e7", "Incline Push-Ups", "Chest", 0, 12, 20, "Hands elevated on a chair or bench.", "incline_pushup"),
                Exercise("e8", "Wide Arm Push-Ups", "Chest", 0, 12, 22, "Hands wider than shoulder width.", "wide_pushup"),
                Exercise("e9", "Mountain Climbers", "Cardio", 40, 0, 30, "Plank position, alternate bringing knees to chest quickly.", "climbers"),
                Exercise("e10", "Diamond Push-Ups", "Triceps", 0, 10, 25, "Form a diamond shape with thumbs and index fingers.", "diamond"),
                Exercise("e11", "Chest Opener Stretch", "Stretch", 30, 0, 10, "Extend arms wide stretching pectoral muscles.", "chest_stretch")
            )
        ),
        WorkoutPlan(
            id = "arm_toning",
            title = "Arm & Shoulder Sculpt",
            category = "Arms",
            level = "Beginner",
            durationMinutes = 12,
            totalCalories = 110,
            exerciseCount = 5,
            exercises = listOf(
                Exercise("e12", "Arm Circles", "Shoulders", 40, 0, 15, "Extend arms out and make small controlled circles.", "circles"),
                Exercise("e13", "Tricep Dips on Chair", "Triceps", 0, 14, 25, "Bend elbows lowering hips in front of a chair.", "dips"),
                Exercise("e14", "Pike Push-Ups", "Shoulders", 0, 10, 25, "Hips raised high, target shoulder muscles.", "pike"),
                Exercise("e15", "Doorframe Bicep Curls", "Biceps", 0, 15, 20, "Hold doorframe leaning back and pull body forward.", "bicep"),
                Exercise("e16", "Shoulder Stretch", "Stretch", 30, 0, 10, "Pull arm across chest gently stretching shoulders.", "shoulder_stretch")
            )
        ),
        WorkoutPlan(
            id = "leg_power",
            title = "Leg & Glute Burner",
            category = "Legs",
            level = "Intermediate",
            durationMinutes = 18,
            totalCalories = 160,
            exerciseCount = 5,
            exercises = listOf(
                Exercise("e17", "Bodyweight Squats", "Quads", 0, 20, 30, "Lower hips down and back keeping chest upright.", "squat"),
                Exercise("e18", "Backward Lunges", "Legs", 0, 16, 28, "Step backward, bend knees to 90 degrees.", "lunges"),
                Exercise("e19", "Glute Bridges", "Glutes", 0, 18, 25, "Lie on back, drive through heels to lift hips.", "bridge"),
                Exercise("e20", "Calf Raises", "Calves", 0, 25, 20, "Lift heels up onto toes, hold briefly and lower.", "calves"),
                Exercise("e21", "Quad Stretch", "Stretch", 30, 0, 10, "Pull foot toward glutes holding balance.", "quad_stretch")
            )
        )
    )
}
