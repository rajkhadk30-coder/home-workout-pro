package com.example.data.repository

import com.example.data.model.*

object ExercisesData {

    val allExercises: List<Exercise> by lazy {
        val list = mutableListOf<Exercise>()

        // 1. Full Body (20 exercises)
        val fullBodyNames = listOf(
            "Jumping Jacks" to "जम्पिङ ज्याक्स",
            "Burpees" to "बर्पीज",
            "High Knees" to "हाई नीज",
            "Mountain Climbers" to "माउन्टेन क्लाइम्बर्स",
            "Squat Jumps" to "स्क्वाट जम्प्स",
            "Push Up to Knee Drive" to "पुश अप र घुँडा तान्ने",
            "Plank Jacks" to "प्लांक ज्याक्स",
            "Bear Crawl" to "बेयर क्रल",
            "Star Jumps" to "स्टार जम्प्स",
            "Skater Jumps" to "स्केटर जम्प्स",
            "Inchworm" to "इन्चवर्म",
            "Tuck Jumps" to "टक जम्प्स",
            "Shadow Boxing" to "स्याडो बक्सिङ",
            "Sponge Rollers" to "स्पन्ज रोलर्स",
            "Cross Jacks" to "क्रस ज्याक्स",
            "Butt Kicks" to "बट किक्स",
            "Side Hop" to "साइड हप",
            "Burpee Tuck Jump" to "बर्पी टक जम्प",
            "Windmill" to "विन्डमिल",
            "Full Body Stretch Hold" to "पूर्ण शरीर स्ट्रेच होल्ड"
        )
        fullBodyNames.forEachIndexed { i, (en, np) ->
            list.add(
                Exercise(
                    id = "fb_${i + 1}",
                    nameEn = en,
                    nameNp = np,
                    descriptionEn = "Engage entire body muscles with continuous fluid movement for maximum calorie burn.",
                    descriptionNp = "अधिकतम क्यालोरी बर्नको लागि निरन्तर गतिशीलताका साथ पूर्ण शरीरका मांसपेशीहरू सक्रिय बनाउनुहोस्।",
                    category = ExerciseCategory.FULL_BODY,
                    targetMuscle = "Full Body",
                    difficulty = if (i % 3 == 0) Difficulty.BEGINNER else if (i % 3 == 1) Difficulty.INTERMEDIATE else Difficulty.ADVANCED,
                    caloriesBurned = 12 + (i % 5) * 3,
                    durationSeconds = 30 + (i % 3) * 10,
                    repetitions = 15 + (i % 4) * 5,
                    restTimeSeconds = 15,
                    voiceGuidanceEn = "Keep core tight and breathe continuously during $en.",
                    voiceGuidanceNp = "$np गर्दा पेट कडा राख्नुहोस् र निरन्तर सास लिनुहोस्।",
                    animationType = if (i % 2 == 0) "JUMPING_JACKS" else "BURPEE"
                )
            )
        }

        // 2. Chest (15 exercises)
        val chestNames = listOf(
            "Standard Push Ups" to "साधारण पुश अप्स",
            "Wide Arm Push Ups" to "चौडा हात पुश अप्स",
            "Diamond Push Ups" to "डाइमण्ड पुश अप्स",
            "Incline Push Ups" to "इन्क्लाइन पुश अप्स",
            "Decline Push Ups" to "डिक्लाइन पुश अप्स",
            "Knee Push Ups" to "घुँडा पुश अप्स",
            "Chest Dips on Chair" to "कुर्सी चेस्ट डिप्स",
            "Pike Push Ups" to "पाइक पुश अप्स",
            "Chest Fly Pullover" to "चेस्ट फ्लाई पुलओभर",
            "Clapping Push Ups" to "ताली पुश अप्स",
            "Staggered Push Ups" to "स्ट्यागर्ड पुश अप्स",
            "Archer Push Ups" to "आर्चर पुश अप्स",
            "Floor Chest Press" to "भुइँ चेस्ट प्रेस",
            "Isometric Chest Squeeze" to "आइसोमेट्रिक चेस्ट स्क्विज",
            "Dynamic Chest Stretch" to "डाइनामिक चेस्ट स्ट्रेच"
        )
        chestNames.forEachIndexed { i, (en, np) ->
            list.add(
                Exercise(
                    id = "chest_${i + 1}",
                    nameEn = en,
                    nameNp = np,
                    descriptionEn = "Target upper, middle, and lower pectorals for upper body strength and posture.",
                    descriptionNp = "माथिल्लो शरीरको शक्ति र बनावटका लागि छातीका माथिल्लो र तल्लो भागलाई लक्षित गर्नुहोस्।",
                    category = ExerciseCategory.CHEST,
                    targetMuscle = "Pectorals",
                    difficulty = if (i < 5) Difficulty.BEGINNER else if (i < 10) Difficulty.INTERMEDIATE else Difficulty.ADVANCED,
                    caloriesBurned = 10 + (i % 4) * 2,
                    durationSeconds = 30,
                    repetitions = 12 + (i % 3) * 4,
                    restTimeSeconds = 15,
                    voiceGuidanceEn = "Lower your chest close to floor and explode upwards.",
                    voiceGuidanceNp = "छाती भुइँको नजिक लैजानुहोस् र बलपूर्वक माथि उठाउनुहोस्।",
                    animationType = "PUSHUP"
                )
            )
        }

        // 3. Arms (15 exercises)
        val armNames = listOf(
            "Tricep Dips" to "ट्राइसेप्ट डिप्स",
            "Diamond Knee Push Ups" to "डाइमण्ड घुँडा पुश अप्स",
            "Bicep Isometric Curl" to "बाइसेप्ट आइसोमेट्रिक कर्ल",
            "Punching Combination" to "पन्चिङ कम्बिनेसन",
            "Arm Circles" to "हात घुमाउने (Arm Circles)",
            "Doorway Bicep Pull" to "ढोका बाइसेप्ट पुल",
            "Tricep Extension Hold" to "ट्राइसेप्ट एक्सटेन्सन होल्ड",
            "Plank to Push Up" to "प्लांक देखि पुश अप",
            "Crab Walk" to "क्याक्टस / क्याब वाक",
            "Cobra Push Ups" to "कोब्रा पुश अप्स",
            "Commando Planks" to "कमाण्डो प्लांक्स",
            "Wall Bicep Curls" to "भित्ता बाइसेप्ट कर्ल्स",
            "Tricep Floor Dips" to "भुइँ ट्राइसेप्ट डिप्स",
            "Bodyweight Hammer Curl" to "ह्यामर कर्ल होल्ड",
            "Wrist Roller Stretch" to "नाडी र हात स्ट्रेच"
        )
        armNames.forEachIndexed { i, (en, np) ->
            list.add(
                Exercise(
                    id = "arm_${i + 1}",
                    nameEn = en,
                    nameNp = np,
                    descriptionEn = "Sculpt biceps, triceps, and forearms with focused resistance movements.",
                    descriptionNp = "बाइसेप्स, ट्राइसेप्स र हातलाई बलियो र आकर्षक बनाउनुहोस्।",
                    category = ExerciseCategory.ARMS,
                    targetMuscle = "Biceps & Triceps",
                    difficulty = if (i % 3 == 0) Difficulty.BEGINNER else if (i % 3 == 1) Difficulty.INTERMEDIATE else Difficulty.ADVANCED,
                    caloriesBurned = 8 + (i % 4) * 2,
                    durationSeconds = 30,
                    repetitions = 15,
                    restTimeSeconds = 12,
                    voiceGuidanceEn = "Squeeze biceps and triceps tightly at peak position.",
                    voiceGuidanceNp = "उच्च बिन्दुमा पुगेपछि बाइसेप्स र ट्राइसेप्समा दबाब दिनुहोस्।",
                    animationType = "DIPS"
                )
            )
        }

        // 4. Abs (20 exercises)
        val absNames = listOf(
            "Abdominal Crunches" to "पेटको क्रन्चेस (Crunches)",
            "Leg Raises" to "खुट्टा उचाल्ने (Leg Raises)",
            "Plank Hold" to "प्लांक होल्ड (Plank Hold)",
            "Russian Twists" to "रसियन ट्विस्ट (Russian Twists)",
            "Bicycle Crunches" to "साइकल क्रन्चेस",
            "Mountain Climber Twists" to "माउन्टेन क्लाइम्बर ट्विस्ट",
            "Heel Touches" to "कुर्कुच्चा छुने (Heel Touches)",
            "Flutter Kicks" to "फ्लटर किक्स",
            "V-Ups" to "भी-अप्स (V-Ups)",
            "Reverse Crunches" to "रिभर्स क्रन्चेस",
            "Side Plank Left" to "साइड प्लांक बाँया",
            "Side Plank Right" to "साइड प्लांक दाँया",
            "Dead Bug" to "डेड बग (Dead Bug)",
            "Hollow Body Hold" to "हलो बडी होल्ड",
            "Toe Touches" to "खुट्टाको औंला छुने",
            "Windshield Wipers" to "विन्डसिल्ड वाइपर्स",
            "Spiderman Plank" to "स्पाइडरम्यान प्लांक",
            "Plank Hip Dips" to "प्लांक हिप डिप्स",
            "Ab Scissor Kicks" to "कैंची किक्स (Scissors)",
            "Cat Cow Core Stretch" to "कोर स्ट्रेच"
        )
        absNames.forEachIndexed { i, (en, np) ->
            list.add(
                Exercise(
                    id = "abs_${i + 1}",
                    nameEn = en,
                    nameNp = np,
                    descriptionEn = "Tone your core, build 6-pack abs, and improve core stability.",
                    descriptionNp = "कोर बलियो बनाउनुहोस्, ६-प्याक एब्स र पेट घटाउने प्रयास गर्नुहोस्।",
                    category = ExerciseCategory.ABS,
                    targetMuscle = "Abs & Core",
                    difficulty = if (i % 3 == 0) Difficulty.BEGINNER else if (i % 3 == 1) Difficulty.INTERMEDIATE else Difficulty.ADVANCED,
                    caloriesBurned = 9 + (i % 3) * 3,
                    durationSeconds = 35,
                    repetitions = 16,
                    restTimeSeconds = 15,
                    voiceGuidanceEn = "Exhale when contracting your ab muscles.",
                    voiceGuidanceNp = "पेटको मांसपेशी खुम्च्याउँदा सास बाहिर फाल्नुहोस्।",
                    animationType = "CRUNCH"
                )
            )
        }

        // 5. Legs (15 exercises)
        val legNames = listOf(
            "Bodyweight Squats" to "साधारण स्क्वाट्स",
            "Forward Lunges" to "अगाडि लन्जेस (Lunges)",
            "Backward Lunges" to "पछाडि लन्जेस",
            "Sumo Squats" to "सुमो स्क्वाट्स",
            "Calf Raises" to "पिँडुला उचाल्ने (Calf Raises)",
            "Wall Sit Hold" to "भित्तामा बस्ने होल्ड (Wall Sit)",
            "Curtsy Lunges" to "कर्टसी लन्जेस",
            "Jump Squats" to "जम्प स्क्वाट्स",
            "Single Leg Glute Bridge" to "एक खुट्टे ग्लुट ब्रिज",
            "Side Lunges" to "साइड लन्जेस",
            "Bulgarian Split Squats" to "बल्गेरियन स्प्लिट स्क्वाट्स",
            "Donkey Kicks" to "डंकी किक्स",
            "Fire Hydrants" to "फायर हाइड्र्यान्ट्स",
            "Glute Bridge Pulse" to "ग्लुट ब्रिज पल्स",
            "Leg Quad Stretch" to "खुट्टा क्वाड स्ट्रेच"
        )
        legNames.forEachIndexed { i, (en, np) ->
            list.add(
                Exercise(
                    id = "leg_${i + 1}",
                    nameEn = en,
                    nameNp = np,
                    descriptionEn = "Strengthen quadriceps, hamstrings, glutes, and calves for explosive lower body power.",
                    descriptionNp = "तिघ्रा, पिँडुला र कम्मरको शक्ति र आकार वृद्धि गर्नुहोस्।",
                    category = ExerciseCategory.LEGS,
                    targetMuscle = "Quadriceps & Glutes",
                    difficulty = if (i < 5) Difficulty.BEGINNER else if (i < 10) Difficulty.INTERMEDIATE else Difficulty.ADVANCED,
                    caloriesBurned = 11 + (i % 4) * 3,
                    durationSeconds = 30,
                    repetitions = 15,
                    restTimeSeconds = 15,
                    voiceGuidanceEn = "Push through heels and keep knees aligned with toes.",
                    voiceGuidanceNp = "कुर्कुच्चामा बल दिनुहोस् र घुँडाको सन्तुलन कायम राख्नुहोस्।",
                    animationType = "SQUAT"
                )
            )
        }

        // 6. Back (15 exercises)
        val backNames = listOf(
            "Superman Hold" to "सुपरम्यान होल्ड (Superman)",
            "Cobra Pose Hold" to "कोब्रा पोज (Cobra Pose)",
            "Reverse Snow Angels" to "रिभर्स स्नो एन्जल्स",
            "Prone Y-T-W Raises" to "प्रोन वाइ-टी-डब्लु रेज",
            "Bird Dog Hold" to "बर्ड डग होल्ड",
            "Hyperextension on Floor" to "भुइँ हाइपरएक्स्टेन्सन",
            "Rhomboid Pulls" to "रोम्बोइड पुल",
            "Doorway Lat Pulldown" to "ढोका ल्याट पुलडाउन",
            "Cat Cow Back Stretch" to "क्याट काउ पीठ स्ट्रेच",
            "Tabletop Back Raise" to "टेबलटप पीठ उचाल्ने",
            "Swimmer Kicks" to "स्विमर किक्स",
            "Dorsal Raises" to "डोर्सल रेजेज",
            "Child's Pose Decompression" to "चाइल्ड्स पोज स्ट्रेच",
            "Good Mornings Bodyweight" to "गुड मर्निङ बडीवेट",
            "Thoracic Spine Rotation" to "थोरासिक स्पाइन रोटेशन"
        )
        backNames.forEachIndexed { i, (en, np) ->
            list.add(
                Exercise(
                    id = "back_${i + 1}",
                    nameEn = en,
                    nameNp = np,
                    descriptionEn = "Improve posture, eliminate lower back pain, and broaden upper back Lats.",
                    descriptionNp = "ढाड र पीठको दुखाइ कम गर्नुहोस्, र सीधा बनावट कायम राख्नुहोस्।",
                    category = ExerciseCategory.BACK,
                    targetMuscle = "Lats & Lower Back",
                    difficulty = if (i % 3 == 0) Difficulty.BEGINNER else if (i % 3 == 1) Difficulty.INTERMEDIATE else Difficulty.ADVANCED,
                    caloriesBurned = 9 + (i % 3) * 2,
                    durationSeconds = 30,
                    repetitions = 12,
                    restTimeSeconds = 15,
                    voiceGuidanceEn = "Squeeze shoulder blades together smoothly.",
                    voiceGuidanceNp = "काँध र पीठका मांसपेशीहरूलाई बिस्तारै जोड्नुहोस्।",
                    animationType = "PLANK"
                )
            )
        }

        // 7. Shoulders (15 exercises)
        val shoulderNames = listOf(
            "Pike Push Ups for Delts" to "पाइक डेल्ट पुश अप्स",
            "Arm Shoulder Presses" to "हात काँध प्रेस",
            "Lateral Deltoid Raises" to "ल्याटरल डेल्टोइड रेज",
            "Front Deltoid Raises" to "फ्रन्ट डेल्टोइड रेज",
            "Reverse Fly Hold" to "रिभर्स फ्लाई होल्ड",
            "Dolphin Push Ups" to "डल्फिन पुश अप्स",
            "Wall Walk Hold" to "भित्ता वाक होल्ड",
            "Bear Crawl Shoulder Taps" to "बेयर क्रल सोल्डर ट्याप्स",
            "Plank Shoulder Taps" to "प्लांक सोल्डर ट्याप्स",
            "Isometric Shoulder Press" to "आइसोमेट्रिक सोल्डर प्रेस",
            "Scapular Push Ups" to "स्क्यापुलर पुश अप्स",
            "Arm Scissors High" to "हात कैंची हाई",
            "Y-Raise Hold" to "वाइ-रेज होल्ड",
            "Shoulder Circles" to "काँध घुमाउने",
            "Cross Body Shoulder Stretch" to "क्रस बडी सोल्डर स्ट्रेच"
        )
        shoulderNames.forEachIndexed { i, (en, np) ->
            list.add(
                Exercise(
                    id = "shoulder_${i + 1}",
                    nameEn = en,
                    nameNp = np,
                    descriptionEn = "Build broad, 3D rounded shoulders and improve overhead stability.",
                    descriptionNp = "काँधका मांसपेशीहरू बलिया र आकर्षक बनाउनुहोस्।",
                    category = ExerciseCategory.SHOULDERS,
                    targetMuscle = "Deltoids",
                    difficulty = if (i % 3 == 0) Difficulty.BEGINNER else if (i % 3 == 1) Difficulty.INTERMEDIATE else Difficulty.ADVANCED,
                    caloriesBurned = 9 + (i % 3) * 2,
                    durationSeconds = 30,
                    repetitions = 12,
                    restTimeSeconds = 12,
                    voiceGuidanceEn = "Control the movement without swinging your torso.",
                    voiceGuidanceNp = "जिउ नहलाई काँध नियन्त्रणका साथ उठाउनुहोस्।",
                    animationType = "PUSHUP"
                )
            )
        }

        // 8. Cardio (15 exercises)
        val cardioNames = listOf(
            "High Knees Sprint" to "हाई नीज स्प्रिन्ट",
            "Jumping Jack Combo" to "जम्पिङ ज्याक कम्बिनेशन",
            "Side to Side Hops" to "साइड टु साइड हप्स",
            "Fast Feet Run in Place" to "फास्ट फीट रन",
            "Shadow Rope Skipping" to "काल्पनिक डोरी फड्किने",
            "Half Burpee Speed" to "हाफ बर्पी स्पीड",
            "Speed Skater Hops" to "स्पीड स्केटर हप्स",
            "Jack Burpees" to "ज्याक बर्पीज",
            "Cross Knee Strikes" to "क्रस नी स्ट्राइक",
            "Side Shuffle Taps" to "साइड सफल्स",
            "Quick Feet Drills" to "क्विक फीट ड्रिल",
            "Boxer Shuffle" to "बक्सर सफल्स",
            "High Jump Reach" to "हाई जम्प रिच",
            "Power Jacks" to "पावर ज्याक्स",
            "Cool Down Jog" to "कुल डाउन जग"
        )
        cardioNames.forEachIndexed { i, (en, np) ->
            list.add(
                Exercise(
                    id = "cardio_${i + 1}",
                    nameEn = en,
                    nameNp = np,
                    descriptionEn = "Boost heart health, stamina, and burn fat quickly with cardio exercises.",
                    descriptionNp = "मुटुको स्वास्थ्य, शक्ति र छिटो बोसो घटाउन कार्डियो अभ्यास गर्नुहोस्।",
                    category = ExerciseCategory.CARDIO,
                    targetMuscle = "Cardiovascular",
                    difficulty = if (i % 3 == 0) Difficulty.BEGINNER else if (i % 3 == 1) Difficulty.INTERMEDIATE else Difficulty.ADVANCED,
                    caloriesBurned = 14 + (i % 4) * 3,
                    durationSeconds = 40,
                    repetitions = 20,
                    restTimeSeconds = 15,
                    voiceGuidanceEn = "Pace yourself and breathe rhythmically.",
                    voiceGuidanceNp = "आफ्नो गति मिलाउनुहोस् र तालबद्ध रूपमा सास फेर्नुहोस्।",
                    animationType = "JUMPING_JACKS"
                )
            )
        }

        // 9. HIIT (15 exercises)
        val hiitNames = listOf(
            "Burpee Tuck Jumps HIIT" to "बर्पी टक जम्प एच.आई.आई.टी",
            "Mountain Climber Sprint" to "माउन्टेन क्लाइम्बर स्प्रिन्ट",
            "Squat Jump Burpees" to "स्क्वाट जम्प बर्पीज",
            "Plank Jack Push Ups" to "प्लांक ज्याक पुश अप्स",
            "Speed High Knees" to "स्पीड हाई नीज",
            "Plyo Lunges" to "प्लायो लन्जेस",
            "180 Degree Jump Squats" to "१८० डिग्री जम्प स्क्वाट्स",
            "Commandos Fast" to "कमाण्डोज फास्ट",
            "Star Jump Explosions" to "स्टार जम्प एक्सप्लोजन",
            "Sprinting On Spot" to "उही ठाउँमा स्प्रिन्ट",
            "Burpee Knee Drive" to "बर्पी नी ड्राइभ",
            "Russian Twist Speed" to "रसियन ट्विस्ट स्पीड",
            "Speed Skaters" to "स्पीड स्केटर्स",
            "Plank Punch Sprint" to "प्लांक पन्च स्प्रिन्ट",
            "Tabata Finish Push" to "टाबाटा फिनिस पुश"
        )
        hiitNames.forEachIndexed { i, (en, np) ->
            list.add(
                Exercise(
                    id = "hiit_${i + 1}",
                    nameEn = en,
                    nameNp = np,
                    descriptionEn = "High Intensity Interval Training for maximum fat oxidation and metabolism spike.",
                    descriptionNp = "द्रुत गतिमा बोसो नष्ट गर्न र शरीरको ऊर्जा बढाउन तीव्र व्यायाम।",
                    category = ExerciseCategory.HIIT,
                    targetMuscle = "Full Body Metabolism",
                    difficulty = Difficulty.ADVANCED,
                    caloriesBurned = 18 + (i % 4) * 3,
                    durationSeconds = 25,
                    repetitions = 20,
                    restTimeSeconds = 10,
                    voiceGuidanceEn = "Give 100 percent effort for these 25 seconds!",
                    voiceGuidanceNp = "यी २५ सेकेन्डमा आफ्नो पूर्ण १०० प्रतिशत क्षमता प्रयोग गर्नुहोस्!",
                    animationType = "BURPEE"
                )
            )
        }

        // 10. Stretching (10 exercises)
        val stretchNames = listOf(
            "Cobra Stretch Hold" to "कोब्रा स्ट्रेच होल्ड",
            "Child Pose Stretch" to "चाइल्ड पोज स्ट्रेच",
            "Standing Hamstring Stretch" to "ह्यामस्ट्रिङ स्ट्रेच",
            "Quad Standing Stretch" to "क्वाड स्ट्रेच",
            "Shoulder Cross Stretch" to "काँध क्रस स्ट्रेच",
            "Tricep Overhead Stretch" to "ट्राइसेप्ट स्ट्रेच",
            "Butterfly Inner Thigh Stretch" to "बटरफ्लाई स्ट्रेच",
            "Seated Spine Twist" to "ढाड ट्विस्ट स्ट्रेच",
            "Calf Wall Stretch" to "पिँडुला भित्ता स्ट्रेच",
            "Full Body Sky Stretch" to "आकाश तर्फ स्ट्रेच"
        )
        stretchNames.forEachIndexed { i, (en, np) ->
            list.add(
                Exercise(
                    id = "stretch_${i + 1}",
                    nameEn = en,
                    nameNp = np,
                    descriptionEn = "Increase mobility, reduce muscle soreness, and prevent injuries.",
                    descriptionNp = "शरीरको लचकता बढाउनुहोस् र मांसपेशीको दुखाइ कम गर्नुहोस्।",
                    category = ExerciseCategory.STRETCHING,
                    targetMuscle = "Full Body Flexibility",
                    difficulty = Difficulty.BEGINNER,
                    caloriesBurned = 5,
                    durationSeconds = 30,
                    repetitions = 1,
                    restTimeSeconds = 10,
                    voiceGuidanceEn = "Hold stretch gently without bouncing.",
                    voiceGuidanceNp = "नझड्कारी बिस्तारै स्ट्रेच होल्ड गर्नुहोस्।",
                    animationType = "PLANK"
                )
            )
        }

        // 11. Warm Up (10 exercises)
        val warmupNames = listOf(
            "Neck Circles Warmup" to "गर्धन घुमाउने",
            "Shoulder Rolls" to "काँध रोल्स",
            "Arm Swings" to "हात हल्लाउने",
            "Hip Circles" to "कम्मर घुमाउने",
            "Light Jog on Spot" to "हल्का जगिङ",
            "Gentle Jumping Jacks" to "हल्का जम्पिङ ज्याक्स",
            "Torso Rotations" to "जिउ घुमाउने",
            "Ankle Circles Left" to "गोडाको कुर्कुच्चा घुमाउने बाँया",
            "Ankle Circles Right" to "गोडाको कुर्कुच्चा घुमाउने दाँया",
            "Deep Breathing Warmup" to "गहिरो सास लिने"
        )
        warmupNames.forEachIndexed { i, (en, np) ->
            list.add(
                Exercise(
                    id = "warmup_${i + 1}",
                    nameEn = en,
                    nameNp = np,
                    descriptionEn = "Prepare joints, raise heart rate slightly, and prime muscles for exercise.",
                    descriptionNp = "व्यायाम अघि जोर्नी र मांसपेशीहरू तयार पार्नुहोस्।",
                    category = ExerciseCategory.WARM_UP,
                    targetMuscle = "Joint Mobility",
                    difficulty = Difficulty.BEGINNER,
                    caloriesBurned = 6,
                    durationSeconds = 25,
                    repetitions = 10,
                    restTimeSeconds = 10,
                    voiceGuidanceEn = "Relax your shoulders and breathe deeply.",
                    voiceGuidanceNp = "काँध खुकुलो पार्नुहोस् र गहिरो सास लिनुहोस्।",
                    animationType = "JUMPING_JACKS"
                )
            )
        }

        list
    }
}
