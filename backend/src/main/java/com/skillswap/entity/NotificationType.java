package com.skillswap.entity;

public enum NotificationType {
    SESSION_REQUESTED,   // mentor gets this when someone requests a session
    SESSION_ACCEPTED,    // requester gets this when mentor accepts
    SESSION_REJECTED,    // requester gets this when mentor rejects
    SESSION_COMPLETED,   // requester gets this when mentor marks complete
    REVIEW_RECEIVED      // mentor gets this when a review is submitted
}
