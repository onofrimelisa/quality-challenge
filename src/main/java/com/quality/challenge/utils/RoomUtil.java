package com.quality.challenge.utils;

import com.quality.challenge.dto.StatusCodeDTO;
import com.quality.challenge.exceptions.InvalidPeopleForRoomException;
import org.springframework.http.HttpStatus;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RoomUtil {
    private static final Map<String, Integer> peoplePerRoom = initialize();

    private static Map<String, Integer> initialize() {
        Map<String, Integer> peoplePerRoomMap = new HashMap<>();
        peoplePerRoomMap.put("single", 1);
        peoplePerRoomMap.put("double", 2);
        peoplePerRoomMap.put("triple", 3);
        peoplePerRoomMap.put("multiple", 10);
        return peoplePerRoomMap;
    }

    public static void correctNumberOfPeopleForRoom(String room, Integer peopleAmount, Integer numberOfPeople) throws InvalidPeopleForRoomException {
        room = Normalizer.normalize(room, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        Integer correctNumber = peoplePerRoom.get(room.toLowerCase(Locale.ROOT));
        if (!numberOfPeople.equals(peopleAmount) || correctNumber == null || peopleAmount > correctNumber){
            StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode("The amount of people is incorrect for the chosen room, or the people sent is different from the amount of people value sent", HttpStatus.BAD_REQUEST);
            throw new InvalidPeopleForRoomException(statusCodeDTO);
        }
    }
}
