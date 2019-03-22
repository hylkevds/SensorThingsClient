package de.fraunhofer.iosb.ilt.sta.model.Constraints;

public class AllowedPatternTokens extends Constraint {
    private String pattern;

    public AllowedPatternTokens(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean isValid(Object input) {
        String inputString = (String) input;
        return inputString.matches(pattern);
    }
}
