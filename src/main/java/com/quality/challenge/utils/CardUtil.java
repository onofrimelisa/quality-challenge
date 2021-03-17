package com.quality.challenge.utils;

import com.quality.challenge.dto.StatusCodeDTO;
import com.quality.challenge.exceptions.InvalidCardDuesException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CardUtil {
    private static final Map<String, Integer[]> validDues = initializeValidDues();
    private static final Map<Integer, Double> validInterest = initializeValidInterest();

    private static Map<String, Integer[]> initializeValidDues() {
        Map<String, Integer[]> validDues = new HashMap<>();
        validDues.put("credit", new Integer[]{1, 3, 6, 9});
        validDues.put("debit", new Integer[]{1});
        return validDues;
    }

    private static Map<Integer, Double> initializeValidInterest(){
        Map<Integer, Double> validInterest = new HashMap<>();
        validInterest.put(1, 0d);
        validInterest.put(3, 5d);
        validInterest.put(6, 10d);
        validInterest.put(9, 15d);
        return validInterest;
    }

    public static void verifyValidCardDues(String card, Integer dues) throws InvalidCardDuesException {
        Integer[] validDuesPerCard = validDues.get(card.toLowerCase(Locale.ROOT));
        if (validDuesPerCard == null || Arrays.stream(validDuesPerCard).noneMatch(due->due.equals(dues))){
            StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode(
                "The dues are not valid for the selected type of card. For credit cards, the valid dues are: 1, 3, 6 and 9; for debit cards, the valid dues are: 1",
                HttpStatus.BAD_REQUEST);
            throw new InvalidCardDuesException(statusCodeDTO);
        }
    }

    public static Double getInterest(Integer dues){
        return validInterest.get(dues);
    }
}
