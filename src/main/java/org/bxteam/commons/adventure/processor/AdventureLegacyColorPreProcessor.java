package org.bxteam.commons.adventure.processor;

import java.util.function.UnaryOperator;

public class AdventureLegacyColorPreProcessor implements UnaryOperator<String> {
    @Override
    public String apply(String s) {
        return s.replace("§", "&");
    }
}
