package ru.practicum.ewm.mainservice.comment.model;

public enum CommentState {
    PUBLISHED("PUBLISHED"),
    REJECTED("REJECTED"),
    PENDING("PENDING");

    private final String state;

    CommentState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public static CommentState fromState(String state) {
        for (CommentState commentState : CommentState.values()) {
            if (commentState.getState().equals(state)) {
                return commentState;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "CommentState{" +
                "state='" + state + '\'' +
                '}';
    }
}