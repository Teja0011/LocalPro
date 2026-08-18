package com.example.data.model

object CategoriesData {
    val allCategories = listOf(
        // Stitching, Tailoring & Boutique Specialization
        ServiceCategory(
            id = "stitching_tailoring",
            name = "Stitching & Boutique Tailor",
            nameHindi = "सिलाई व बुटीक टेलरिंग",
            nameTelugu = "టైలరింగ్ & కుట్టు పనులు (బుటీక్)",
            iconEmoji = "🧵",
            colorHex = 0xFFD81B60,
            avgRate = "₹149",
            unit = "/piece",
            sortOrder = 0
        ),

        // Caretaker & Health Support
        ServiceCategory(
            id = "caretaker",
            name = "Caretaker (Elderly Care)",
            nameHindi = "बुजुर्ग देखभाल (केयरटेकर)",
            nameTelugu = "వృద్ధుల సంరక్షణ (కేర్‌టేకర్)",
            iconEmoji = "🧓",
            colorHex = 0xFF7E57C2,
            avgRate = "₹250",
            unit = "/hr",
            sortOrder = 1
        ),
        ServiceCategory(
            id = "babysitter",
            name = "Baby Sitter / Nanny",
            nameHindi = "शिशु देखभाल (आया)",
            nameTelugu = "బేబీ సిట్టర్ / ఆయా",
            iconEmoji = "👶",
            colorHex = 0xFFFF8A65,
            avgRate = "₹220",
            unit = "/hr",
            sortOrder = 2
        ),
        ServiceCategory(
            id = "home_nurse",
            name = "Home Nurse & Patient Care",
            nameHindi = "होम नर्स व मरीज देखभाल",
            nameTelugu = "హోమ్ నర్స్ & పేషెంట్ కేర్",
            iconEmoji = "🩺",
            colorHex = 0xFF00ACC1,
            avgRate = "₹350",
            unit = "/hr",
            sortOrder = 3
        ),
        ServiceCategory(
            id = "special_needs_care",
            name = "Special Needs Care",
            nameHindi = "विशेष आवश्यकता देखभाल",
            nameTelugu = "ప్రత్యేక అవసరాల సంరక్షణ",
            iconEmoji = "🤝",
            colorHex = 0xFF5C6BC0,
            avgRate = "₹300",
            unit = "/hr",
            sortOrder = 4
        ),

        // Domestic & House Help
        ServiceCategory(
            id = "house_maid",
            name = "House Maid",
            nameHindi = "घरेलू सहायिका (कामवाली)",
            nameTelugu = "ఇంటి పనిమనిషి",
            iconEmoji = "🏠",
            colorHex = 0xFF66BB6A,
            avgRate = "₹180",
            unit = "/hr",
            sortOrder = 5
        ),
        ServiceCategory(
            id = "cook",
            name = "Home Cook / Chef",
            nameHindi = "रसोइया / बावर्ची",
            nameTelugu = "వంటమనిషి / చెఫ్",
            iconEmoji = "👨‍🍳",
            colorHex = 0xFFFF7043,
            avgRate = "₹250",
            unit = "/hr",
            sortOrder = 6
        ),
        ServiceCategory(
            id = "home_cleaner",
            name = "Home Deep Cleaner",
            nameHindi = "घर की गहरी सफाई",
            nameTelugu = "ఇంటి డీప్ క్లీనింగ్",
            iconEmoji = "🧹",
            colorHex = 0xFF26A69A,
            avgRate = "₹200",
            unit = "/hr",
            sortOrder = 7
        ),
        ServiceCategory(
            id = "sofa_cleaning",
            name = "Sofa & Carpet Cleaning",
            nameHindi = "सोफा व कारपेट सफाई",
            nameTelugu = "సోఫా & కార్పెట్ క్లీనింగ్",
            iconEmoji = "🛋️",
            colorHex = 0xFF8D6E63,
            avgRate = "₹450",
            unit = "/visit",
            sortOrder = 8
        ),
        ServiceCategory(
            id = "laundry",
            name = "Laundry Worker",
            nameHindi = "धोबी / लॉन्ड्री",
            nameTelugu = "లాండ్రీ వర్కర్",
            iconEmoji = "👕",
            colorHex = 0xFF5C6BC0,
            avgRate = "₹150",
            unit = "/hr",
            sortOrder = 9
        ),
        ServiceCategory(
            id = "ironing",
            name = "Ironing Service",
            nameHindi = "इस्त्री सेवा",
            nameTelugu = "ఇస్త్రీ సర్వీస్",
            iconEmoji = "🪡",
            colorHex = 0xFFEC407A,
            avgRate = "₹100",
            unit = "/hr",
            sortOrder = 10
        ),

        // Core Home Repairs
        ServiceCategory(
            id = "electrician",
            name = "Electrician",
            nameHindi = "इलेक्ट्रीशियन",
            nameTelugu = "ఎలక్ట్రీషియన్",
            iconEmoji = "⚡",
            colorHex = 0xFFFFB300,
            avgRate = "₹350",
            unit = "/hr",
            sortOrder = 11
        ),
        ServiceCategory(
            id = "plumber",
            name = "Plumber",
            nameHindi = "प्लंबर",
            nameTelugu = "ప్లంబర్",
            iconEmoji = "🔧",
            colorHex = 0xFF42A5F5,
            avgRate = "₹300",
            unit = "/hr",
            sortOrder = 12
        ),
        ServiceCategory(
            id = "carpenter",
            name = "Carpenter",
            nameHindi = "बढ़ई",
            nameTelugu = "వడ్రంగి",
            iconEmoji = "🪵",
            colorHex = 0xFF8D6E63,
            avgRate = "₹450",
            unit = "/hr",
            sortOrder = 13
        ),
        ServiceCategory(
            id = "painter",
            name = "Painter & Waterproofing",
            nameHindi = "पेंटर व वाटरप्रूफिंग",
            nameTelugu = "పెయింటర్ & వాటర్‌ప్రూఫింగ్",
            iconEmoji = "🎨",
            colorHex = 0xFFFF7043,
            avgRate = "₹250",
            unit = "/hr",
            sortOrder = 14
        ),
        ServiceCategory(
            id = "pest_control",
            name = "Pest Control",
            nameHindi = "कीट नियंत्रण",
            nameTelugu = "కీటక నియంత్రణ",
            iconEmoji = "🐛",
            colorHex = 0xFF8BC34A,
            avgRate = "₹600",
            unit = "/visit",
            sortOrder = 15
        ),
        ServiceCategory(
            id = "gardener",
            name = "Gardener",
            nameHindi = "माली",
            nameTelugu = "తోటమాలి",
            iconEmoji = "🌱",
            colorHex = 0xFF66BB6A,
            avgRate = "₹200",
            unit = "/hr",
            sortOrder = 16
        ),
        ServiceCategory(
            id = "mason",
            name = "Mason & Tile Worker",
            nameHindi = "राजमिस्त्री व टाइल्स",
            nameTelugu = "మేస్త్రీ & టైల్స్ వర్క్",
            iconEmoji = "🧱",
            colorHex = 0xFF795548,
            avgRate = "₹400",
            unit = "/hr",
            sortOrder = 17
        ),
        ServiceCategory(
            id = "locksmith",
            name = "Locksmith & Key Maker",
            nameHindi = "ताला चाबी मरम्मत",
            nameTelugu = "తాళాలు & లాక్‌స్మిత్",
            iconEmoji = "🔑",
            colorHex = 0xFF607D8B,
            avgRate = "₹200",
            unit = "/visit",
            sortOrder = 18
        ),

        // Appliances & Cooling
        ServiceCategory(
            id = "ac_repair",
            name = "AC Repair & Service",
            nameHindi = "एसी मरम्मत",
            nameTelugu = "ఏసీ రిపేర్",
            iconEmoji = "❄️",
            colorHex = 0xFF42A5F5,
            avgRate = "₹400",
            unit = "/hr",
            sortOrder = 19
        ),
        ServiceCategory(
            id = "fridge_repair",
            name = "Refrigerator Repair",
            nameHindi = "फ्रिज मरम्मत",
            nameTelugu = "ఫ్రిజ్ రిపేర్",
            iconEmoji = "🧊",
            colorHex = 0xFF26A69A,
            avgRate = "₹350",
            unit = "/hr",
            sortOrder = 20
        ),
        ServiceCategory(
            id = "washing_machine",
            name = "Washing Machine Repair",
            nameHindi = "वाशिंग मशीन मरम्मत",
            nameTelugu = "వాషింగ్ మెషిన్ రిపేర్",
            iconEmoji = "🧺",
            colorHex = 0xFF1E88E5,
            avgRate = "₹350",
            unit = "/hr",
            sortOrder = 21
        ),
        ServiceCategory(
            id = "ro_service",
            name = "RO & Water Purifier",
            nameHindi = "आरओ वाटर प्यूरीफायर",
            nameTelugu = "ఆర్.ఓ వాటర్ ప్యూరిఫైయర్",
            iconEmoji = "🚰",
            colorHex = 0xFF00ACC1,
            avgRate = "₹300",
            unit = "/visit",
            sortOrder = 22
        ),
        ServiceCategory(
            id = "geyser_repair",
            name = "Geyser / Water Heater",
            nameHindi = "गीजर मरम्मत",
            nameTelugu = "గీజర్ రిపేర్",
            iconEmoji = "♨️",
            colorHex = 0xFFFF5722,
            avgRate = "₹300",
            unit = "/visit",
            sortOrder = 23
        ),
        ServiceCategory(
            id = "gas_stove_repair",
            name = "Gas Stove & Hob Repair",
            nameHindi = "गैस चूल्हा मरम्मत",
            nameTelugu = "గ్యాస్ స్టవ్ రిపేర్",
            iconEmoji = "🔥",
            colorHex = 0xFFD84315,
            avgRate = "₹250",
            unit = "/visit",
            sortOrder = 24
        ),

        // Vehicles & Transport
        ServiceCategory(
            id = "driver",
            name = "Personal Driver",
            nameHindi = "पर्सनल ड्राइवर",
            nameTelugu = "పర్సనల్ డ్రైవర్",
            iconEmoji = "🚘",
            colorHex = 0xFF455A64,
            avgRate = "₹200",
            unit = "/hr",
            sortOrder = 25
        ),
        ServiceCategory(
            id = "mechanic",
            name = "Vehicle Mechanic",
            nameHindi = "गाड़ी मैकेनिक",
            nameTelugu = "వాహన మెకానిక్",
            iconEmoji = "🚗",
            colorHex = 0xFF8D6E63,
            avgRate = "₹300",
            unit = "/hr",
            sortOrder = 26
        ),
        ServiceCategory(
            id = "car_wash",
            name = "Doorstep Car Wash",
            nameHindi = "डोरस्टेप कार वॉश",
            nameTelugu = "డోర్‌స్టెప్ కార్ వాష్",
            iconEmoji = "🚙",
            colorHex = 0xFF0288D1,
            avgRate = "₹250",
            unit = "/trip",
            sortOrder = 27
        ),
        ServiceCategory(
            id = "packers_movers",
            name = "Packers & Movers",
            nameHindi = "पैकर्स एंड मूवर्स",
            nameTelugu = "ప్యాకర్స్ & మూవర్స్",
            iconEmoji = "📦",
            colorHex = 0xFFF57C00,
            avgRate = "₹1500",
            unit = "/trip",
            sortOrder = 28
        ),

        // Daily Essentials & Delivery
        ServiceCategory(
            id = "milk_delivery",
            name = "Milk Delivery",
            nameHindi = "दूध वितरण",
            nameTelugu = "పాల డెలివరీ",
            iconEmoji = "🥛",
            colorHex = 0xFF29B6F6,
            avgRate = "₹50",
            unit = "/trip",
            sortOrder = 29
        ),
        ServiceCategory(
            id = "water_delivery",
            name = "Mineral Water (20L Jar)",
            nameHindi = "मिनरल वॉटर जार",
            nameTelugu = "మినరల్ వాటర్ జార్",
            iconEmoji = "💧",
            colorHex = 0xFF26C6DA,
            avgRate = "₹80",
            unit = "/jar",
            sortOrder = 30
        ),

        // Personal Wellness & Assistance
        ServiceCategory(
            id = "beautician",
            name = "Beautician & Salon",
            nameHindi = "ब्यूटीशियन व सैलून",
            nameTelugu = "బ్యూటీషియన్ & సెలూన్",
            iconEmoji = "💄",
            colorHex = 0xFFEC407A,
            avgRate = "₹500",
            unit = "/hr",
            sortOrder = 31
        ),
        ServiceCategory(
            id = "mehendi",
            name = "Mehendi Artist",
            nameHindi = "मेहंदी आर्टिस्ट",
            nameTelugu = "మెహందీ ఆర్టిస్ట్",
            iconEmoji = "🌿",
            colorHex = 0xFFAB47BC,
            avgRate = "₹300",
            unit = "/hr",
            sortOrder = 32
        ),
        ServiceCategory(
            id = "pet_care",
            name = "Pet Care & Dog Walker",
            nameHindi = "पेट केयर व डॉग वॉकर",
            nameTelugu = "పెట్ కేర్ & డాగ్ వాకర్",
            iconEmoji = "🐕",
            colorHex = 0xFF8D6E63,
            avgRate = "₹180",
            unit = "/hr",
            sortOrder = 33
        ),
        ServiceCategory(
            id = "yoga_trainer",
            name = "Yoga & Fitness Coach",
            nameHindi = "योग व फिटनेस कोच",
            nameTelugu = "యోగా & ఫిట్‌నెస్ కోచ్",
            iconEmoji = "🧘",
            colorHex = 0xFF00897B,
            avgRate = "₹400",
            unit = "/hr",
            sortOrder = 34
        ),
        ServiceCategory(
            id = "home_tutor",
            name = "Home Tutor & Teacher",
            nameHindi = "होम ट्यूटर / शिक्षक",
            nameTelugu = "హోమ్ ట్యూటర్",
            iconEmoji = "📚",
            colorHex = 0xFF3949AB,
            avgRate = "₹300",
            unit = "/hr",
            sortOrder = 35
        ),
        ServiceCategory(
            id = "security_guard",
            name = "Security Guard",
            nameHindi = "सुरक्षा गार्ड व चौकीदार",
            nameTelugu = "సెక్యూరిటీ గార్డ్",
            iconEmoji = "🛡️",
            colorHex = 0xFF37474F,
            avgRate = "₹180",
            unit = "/hr",
            sortOrder = 36
        ),
        ServiceCategory(
            id = "event_helper",
            name = "Event & Party Helper",
            nameHindi = "इवेंट व पार्टी हेल्पर",
            nameTelugu = "ఈవెంట్ & పార్టీ హెల్పర్",
            iconEmoji = "🎉",
            colorHex = 0xFFE91E63,
            avgRate = "₹250",
            unit = "/hr",
            sortOrder = 37
        ),
        ServiceCategory(
            id = "tailor",
            name = "Doorstep Tailor",
            nameHindi = "टेलरिंग व आल्टरेशन",
            nameTelugu = "టైలరింగ్ సర్వీస్",
            iconEmoji = "✂️",
            colorHex = 0xFF673AB7,
            avgRate = "₹200",
            unit = "/hr",
            sortOrder = 38
        )
    )

    fun getCategoryById(id: String): ServiceCategory? {
        return allCategories.find { it.id == id }
    }
}
