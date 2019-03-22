package de.fraunhofer.iosb.ilt.sta.model;

public enum TaskState {
    ACTIVE, SUCCESSFUL, FAILED, NEW, UNKNOWN;
    public String toString() {
        switch (this) {
            case NEW: return "NEW";
            case ACTIVE: return "ACTIVE";
            case FAILED: return "FAILED";
            case SUCCESSFUL: return "SUCCESSFULL";
            case UNKNOWN: return "UNKNOWN";
            default: return "UNKNOWN";
        }
    }

}
