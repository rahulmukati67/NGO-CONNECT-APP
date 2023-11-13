package com.project.ngoconnectapp

data class Feedback(val timestamp: Long, val userFeedback: String) {
    // Add additional properties or methods if needed

    // Empty constructor for Firebase
    constructor() : this(0, "")
}

