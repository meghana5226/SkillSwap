package com.skillswap.entity;

public enum SessionStatus {
    PENDING,    // requested, awaiting mentor response
    ACCEPTED,   // mentor accepted, session is scheduled/ongoing
    REJECTED,   // mentor declined
    CANCELLED,  // requester cancelled before mentor responded
    COMPLETED   // session happened, eligible for review
}
