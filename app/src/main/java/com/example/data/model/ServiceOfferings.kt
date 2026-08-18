package com.example.data.model

enum class BudgetTier(val title: String, val badge: String, val colorHex: Long) {
    BUDGET("Budget Friendly", "🏷️ Economy", 0xFF10B981),
    STANDARD("Standard Value", "⭐ Popular", 0xFF1D4ED8),
    PREMIUM("Premium / Designer", "👑 Deluxe", 0xFF8B5CF6)
}

data class ServiceItem(
    val id: String,
    val categoryId: String,
    val name: String,
    val nameHindi: String = "",
    val nameTelugu: String = "",
    val description: String = "",
    val budgetTier: BudgetTier = BudgetTier.STANDARD,
    val baseCost: Double,
    val unitLabel: String = "/ piece",
    val iconEmoji: String = "✨",
    val estimatedMinutes: Int = 45
)

object ServiceOfferingsData {
    val allOfferings: List<ServiceItem> = listOf(
        // ================= STITCHING & TAILORING SERVICES =================
        ServiceItem(
            id = "stitch_alteration",
            categoryId = "stitching_tailoring",
            name = "Basic Dress Alteration & Fitting",
            nameHindi = "कपड़ों की फिटिंग व आल्टरेशन",
            nameTelugu = "డ్రెస్ ఫిట్టింగ్ & ఆల్టరేషన్",
            description = "Tightening, loosening, waist adjustment, sleeve shortening or length adjustment",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 99.0,
            unitLabel = "/ garment",
            iconEmoji = "✂️",
            estimatedMinutes = 30
        ),
        ServiceItem(
            id = "stitch_saree_pico",
            categoryId = "stitching_tailoring",
            name = "Saree Fall & Pico / Kuchu",
            nameHindi = "साड़ी फॉल व पिको",
            nameTelugu = "చీర ఫాల్ & పీకో / కుచ్చులు",
            description = "Neat matching thread fall stitching, rolled hem pico & designer tassels (kuchu)",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 149.0,
            unitLabel = "/ saree",
            iconEmoji = "🥻",
            estimatedMinutes = 45
        ),
        ServiceItem(
            id = "stitch_simple_blouse",
            categoryId = "stitching_tailoring",
            name = "Simple Saree Blouse (With Lining)",
            nameHindi = "सादा ब्लाउज सिलाई (अस्तर सहित)",
            nameTelugu = "సాదా బ్లౌజ్ కుట్టు (లైనింగ్‌తో)",
            description = "Custom measurement, perfect neck depth, hook/eye placket with premium cotton lining",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 299.0,
            unitLabel = "/ blouse",
            iconEmoji = "🧵",
            estimatedMinutes = 60
        ),
        ServiceItem(
            id = "stitch_kurti",
            categoryId = "stitching_tailoring",
            name = "Dailywear Kurti / Top Stitching",
            nameHindi = "कुर्ती व टॉप सिलाई",
            nameTelugu = "కుర్తీ / టాప్ కుట్టు",
            description = "Straight cut or A-line kurti with side slits, neck piping & custom sleeve styling",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 349.0,
            unitLabel = "/ kurti",
            iconEmoji = "👗",
            estimatedMinutes = 75
        ),
        ServiceItem(
            id = "stitch_salwar_suit",
            categoryId = "stitching_tailoring",
            name = "Salwar Kameez / Punjabi Suit Set",
            nameHindi = "सलवार सूट सेट सिलाई",
            nameTelugu = "సల్వార్ కమీజ్ సూట్ సెట్",
            description = "Complete 2-piece/3-piece suit with pant/patiala, kameez lining & dupatta borders",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 599.0,
            unitLabel = "/ set",
            iconEmoji = "🪡",
            estimatedMinutes = 90
        ),
        ServiceItem(
            id = "stitch_designer_blouse",
            categoryId = "stitching_tailoring",
            name = "Princess Cut / Designer Back Blouse",
            nameHindi = "डिज़ाइनर ब्लाउज (प्रिंसेस कट / पैडेड)",
            nameTelugu = "డిజైనర్ ప్రిన్సెస్ కట్ బ్లౌజ్",
            description = "Padded cups, boat neck/deep back, tie-up dori with latkans & contrast piping",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 649.0,
            unitLabel = "/ blouse",
            iconEmoji = "👚",
            estimatedMinutes = 90
        ),
        ServiceItem(
            id = "stitch_maggam_blouse",
            categoryId = "stitching_tailoring",
            name = "Bridal Maggam & Aari Handwork Blouse",
            nameHindi = "मगगम व आरी वर्क ब्राइडल ब्लाउज",
            nameTelugu = "మగ్గం & ఆరి వర్క్ బ్రైడల్ బ్లౌజ్",
            description = "Intricate zardozi, pearls, kundan, cut-work sleeves with heavy bridal finish",
            budgetTier = BudgetTier.PREMIUM,
            baseCost = 1299.0,
            unitLabel = "/ blouse",
            iconEmoji = "👑",
            estimatedMinutes = 180
        ),
        ServiceItem(
            id = "stitch_lehenga_gown",
            categoryId = "stitching_tailoring",
            name = "Custom Lehenga / Anarkali Gown",
            nameHindi = "कस्टम लहंगा व अनारकली गाउन",
            nameTelugu = "లెహంగా / అనార్కలి గౌన్",
            description = "High flare can-can netting, heavy dupatta tassels, designer waistband & tailored blouse",
            budgetTier = BudgetTier.PREMIUM,
            baseCost = 1499.0,
            unitLabel = "/ lehenga",
            iconEmoji = "💃",
            estimatedMinutes = 150
        ),
        ServiceItem(
            id = "stitch_men_shirt",
            categoryId = "stitching_tailoring",
            name = "Men's Tailored Formal/Casual Shirt",
            nameHindi = "पुरुषों की शर्ट सिलाई",
            nameTelugu = "మెన్స్ షర్ట్ కుట్టు",
            description = "Crisp fused collar, cuffs, chest pocket with tailored slim or regular fit",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 399.0,
            unitLabel = "/ shirt",
            iconEmoji = "👔",
            estimatedMinutes = 60
        ),
        ServiceItem(
            id = "stitch_men_trouser",
            categoryId = "stitching_tailoring",
            name = "Men's Formal Trousers / Chinos",
            nameHindi = "पुरुषों की पैंट / ट्राउजर",
            nameTelugu = "మెన్స్ ఫార్మల్ ట్రౌజర్స్",
            description = "Perfect rise & inseam, waistband curtain lining, coin pocket & sturdy bar tacks",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 449.0,
            unitLabel = "/ trouser",
            iconEmoji = "👖",
            estimatedMinutes = 75
        ),
        ServiceItem(
            id = "stitch_curtains",
            categoryId = "stitching_tailoring",
            name = "Home Curtains & Cushion Covers",
            nameHindi = "पर्दे व कुशन कवर सिलाई",
            nameTelugu = "కర్టెన్స్ & కుషన్ కవర్స్",
            description = "Eyelet metal rings, pinch pleats, zip cushion covers with neat edge hem",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 199.0,
            unitLabel = "/ pair",
            iconEmoji = "🪟",
            estimatedMinutes = 45
        ),
        ServiceItem(
            id = "stitch_doorstep_pickup",
            categoryId = "stitching_tailoring",
            name = "Doorstep Measurement & Sample Pickup",
            nameHindi = "घर पर नाप व कपड़ा पिकअप",
            nameTelugu = "డోర్‌స్టెప్ కొలతలు & ఫాబ్రిక్ పికప్",
            description = "Expert tailor visits with measuring tape, styling book & collects sample dress",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 149.0,
            unitLabel = "/ visit",
            iconEmoji = "🛵",
            estimatedMinutes = 30
        ),

        // Also alias with 'tailor' for compatibility
        ServiceItem(
            id = "tailor_alteration",
            categoryId = "tailor",
            name = "Basic Dress Alteration & Fitting",
            description = "Tightening, loosening, waist adjustment, sleeve shortening or length adjustment",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 99.0,
            unitLabel = "/ garment",
            iconEmoji = "✂️"
        ),
        ServiceItem(
            id = "tailor_saree_pico",
            categoryId = "tailor",
            name = "Saree Fall & Pico / Kuchu",
            description = "Neat matching thread fall stitching, rolled hem pico & designer tassels",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 149.0,
            unitLabel = "/ saree",
            iconEmoji = "🥻"
        ),
        ServiceItem(
            id = "tailor_blouse",
            categoryId = "tailor",
            name = "Simple Saree Blouse (With Lining)",
            description = "Custom measurement, perfect neck depth, hook/eye placket",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 299.0,
            unitLabel = "/ blouse",
            iconEmoji = "🧵"
        ),
        ServiceItem(
            id = "tailor_designer_blouse",
            categoryId = "tailor",
            name = "Princess Cut / Designer Back Blouse",
            description = "Padded cups, boat neck/deep back with contrast piping",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 649.0,
            unitLabel = "/ blouse",
            iconEmoji = "👚"
        ),
        ServiceItem(
            id = "tailor_suit",
            categoryId = "tailor",
            name = "Salwar Kameez / Punjabi Suit Set",
            description = "Complete 3-piece suit with pant/patiala & dupatta borders",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 599.0,
            unitLabel = "/ set",
            iconEmoji = "🪡"
        ),
        ServiceItem(
            id = "tailor_maggam",
            categoryId = "tailor",
            name = "Bridal Maggam & Aari Handwork Blouse",
            description = "Intricate zardozi, pearls, kundan, cut-work sleeves",
            budgetTier = BudgetTier.PREMIUM,
            baseCost = 1299.0,
            unitLabel = "/ blouse",
            iconEmoji = "👑"
        ),

        // ================= ELECTRICIAN SERVICES =================
        ServiceItem(
            id = "elec_switch_repair",
            categoryId = "electrician",
            name = "Switch / Socket Replacement",
            description = "Fix burnt, loose switches, 16A power points or modular plates",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 149.0,
            unitLabel = "/ switch",
            iconEmoji = "🔌"
        ),
        ServiceItem(
            id = "elec_fan_install",
            categoryId = "electrician",
            name = "Ceiling Fan Installation & Uninstallation",
            description = "Secure rod mounting, canopy fitting, blade balancing & regulator wiring",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 199.0,
            unitLabel = "/ fan",
            iconEmoji = "🌀"
        ),
        ServiceItem(
            id = "elec_light_fitting",
            categoryId = "electrician",
            name = "LED Tube / Chandelier / Spot Light Fitting",
            description = "Ceiling drilling, concealed driver installation & warm/cool lighting setup",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 179.0,
            unitLabel = "/ light",
            iconEmoji = "💡"
        ),
        ServiceItem(
            id = "elec_inverter_check",
            categoryId = "electrician",
            name = "Inverter & Battery Wiring / Setup",
            description = "Main DB bypass wiring, battery terminal cleaning, acid top-up & trip check",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 499.0,
            unitLabel = "/ setup",
            iconEmoji = "🔋"
        ),
        ServiceItem(
            id = "elec_short_circuit",
            categoryId = "electrician",
            name = "Short Circuit & MCB Tripping Diagnostic",
            description = "Full home phase inspection, insulation tester diagnostic & MCB box overhaul",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 399.0,
            unitLabel = "/ visit",
            iconEmoji = "⚡"
        ),
        ServiceItem(
            id = "elec_full_house_rewire",
            categoryId = "electrician",
            name = "Complete Flat / Villa Concealed Wiring",
            description = "Heavy duty copper wire drawing, earthing pit installation & DB box paneling",
            budgetTier = BudgetTier.PREMIUM,
            baseCost = 1499.0,
            unitLabel = "/ BHK",
            iconEmoji = "🏗️"
        ),

        // ================= PLUMBER SERVICES =================
        ServiceItem(
            id = "plumb_tap_repair",
            categoryId = "plumber",
            name = "Tap Leakage & Spindle Replacement",
            description = "Fix dripping mixer, pillar tap, angle valve or ceramic cartridge",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 149.0,
            unitLabel = "/ tap",
            iconEmoji = "🚰"
        ),
        ServiceItem(
            id = "plumb_drain_unblock",
            categoryId = "plumber",
            name = "Sink & Basin Drain Unclogging",
            description = "Manual snake wire unclogging, trap pipe cleaning & odorless drain seal",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 249.0,
            unitLabel = "/ drain",
            iconEmoji = "🚿"
        ),
        ServiceItem(
            id = "plumb_commode_repair",
            categoryId = "plumber",
            name = "Flush Tank & Commode Leakage Fix",
            description = "Syphon valve replacement, ball cock repair, jet spray & seat cover fitting",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 349.0,
            unitLabel = "/ toilet",
            iconEmoji = "🚽"
        ),
        ServiceItem(
            id = "plumb_geyser_install",
            categoryId = "plumber",
            name = "Water Geyser Installation & Plumbing",
            description = "Wall hanging, braided connection pipes, pressure release valve & safety check",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 399.0,
            unitLabel = "/ geyser",
            iconEmoji = "♨️"
        ),
        ServiceItem(
            id = "plumb_motor_pump",
            categoryId = "plumber",
            name = "Water Motor Pump & Sump Line Repair",
            description = "Impeller check, check valve replacement, foot valve & priming line fix",
            budgetTier = BudgetTier.PREMIUM,
            baseCost = 699.0,
            unitLabel = "/ pump",
            iconEmoji = "⚙️"
        ),

        // ================= AC REPAIR & SERVICE =================
        ServiceItem(
            id = "ac_foam_jet_service",
            categoryId = "ac_repair",
            name = "Foam Jet Deep AC Cleaning (Split/Window)",
            description = "High pressure indoor/outdoor coil washing, filter cleaning & antimicrobial foam",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 449.0,
            unitLabel = "/ AC",
            iconEmoji = "❄️"
        ),
        ServiceItem(
            id = "ac_gas_refill",
            categoryId = "ac_repair",
            name = "Full Gas Refill (R32 / R410A / R22)",
            description = "Nitrogen pressure leak test, vacuuming, braze repair & 100% genuine refrigerant charging",
            budgetTier = BudgetTier.PREMIUM,
            baseCost = 1499.0,
            unitLabel = "/ AC",
            iconEmoji = "💨"
        ),
        ServiceItem(
            id = "ac_uninstallation_install",
            categoryId = "ac_repair",
            name = "AC Installation & Copper Piping",
            description = "Core wall hole, sturdy outdoor bracket mounting, flare sealing & cooling test",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 899.0,
            unitLabel = "/ AC",
            iconEmoji = "🛠️"
        ),

        // ================= HOME CLEANING =================
        ServiceItem(
            id = "clean_bathroom",
            categoryId = "home_cleaner",
            name = "Bathroom Deep Descaling & Stain Removal",
            description = "Floor tile scrub, mirror polish, tap descaling & anti-bacterial sanitization",
            budgetTier = BudgetTier.BUDGET,
            baseCost = 299.0,
            unitLabel = "/ bathroom",
            iconEmoji = "🧼"
        ),
        ServiceItem(
            id = "clean_kitchen",
            categoryId = "home_cleaner",
            name = "Kitchen Degreasing & Chimney Scrub",
            description = "Countertop, tile oil stain removal, gas stove cleanup & cabinet wiping",
            budgetTier = BudgetTier.STANDARD,
            baseCost = 599.0,
            unitLabel = "/ kitchen",
            iconEmoji = "🍳"
        ),
        ServiceItem(
            id = "clean_full_home",
            categoryId = "home_cleaner",
            name = "Full House Deep Cleaning (1/2 BHK)",
            description = "Balcony, windows, floor single-disc machine buffing, ceiling cobweb removal",
            budgetTier = BudgetTier.PREMIUM,
            baseCost = 1399.0,
            unitLabel = "/ home",
            iconEmoji = "✨"
        )
    )

    fun getOfferingsForCategory(categoryId: String): List<ServiceItem> {
        val direct = allOfferings.filter { it.categoryId == categoryId }
        if (direct.isNotEmpty()) return direct

        // Default generic budget tiers if specific items not yet defined
        return listOf(
            ServiceItem(
                id = "${categoryId}_budget",
                categoryId = categoryId,
                name = "Quick Inspection & Minor Fix",
                description = "Standard 30-minute diagnosis and minor adjustment or repair",
                budgetTier = BudgetTier.BUDGET,
                baseCost = 199.0,
                unitLabel = "/ service",
                iconEmoji = "🏷️"
            ),
            ServiceItem(
                id = "${categoryId}_standard",
                categoryId = categoryId,
                name = "Standard Complete Service",
                description = "Full 1-2 hour comprehensive service with warranty and testing",
                budgetTier = BudgetTier.STANDARD,
                baseCost = 449.0,
                unitLabel = "/ service",
                iconEmoji = "⭐"
            ),
            ServiceItem(
                id = "${categoryId}_premium",
                categoryId = categoryId,
                name = "Heavy Duty / Master Overhaul",
                description = "Premium service with priority emergency scheduling & extended 60-day guarantee",
                budgetTier = BudgetTier.PREMIUM,
                baseCost = 899.0,
                unitLabel = "/ service",
                iconEmoji = "👑"
            )
        )
    }
}
