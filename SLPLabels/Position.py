class Position():
    POSITIONS = ["Other.", 
                "lying on back, hands by side",
                "lying on back, hands under head",
                "lying on stomach, hands by side",
                "lying on stomach, hands under head.",
                "lying on left side, hands by side",
                "lying on left side, hands under head",
                "lying on right side, hands by side",
                "lying on right side, hands under head."] #0, 1, 2, 3, 4, 5, 6, 7, 8
    S1_POSITIONS = ["other", "back", "stomach", "left side", "right side"] #0, 1, 2, 3, 4
    S2_POSITIONS = ["other", "hands by side", "Hands under head"] #0, 1, 2
  

    def getPosDescription(pos:int):
        return Position.POSITIONS[pos]

    def getPos(s1_pos:int, s2_pos:int):
        #if empty or 0 for either, than the position is 0
        if (not s1_pos) or (not s2_pos):
            return 0
        return 2 * (s1_pos - 1)  + s2_pos