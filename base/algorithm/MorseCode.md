莫斯密码实现。
# Java版
```java
public class MorseCode{
    private final static String MORSE_CODE_JSON = "{\"A\": \"•—\", \"()\": \"—•——•—\", \"B\": \"—•••\", \"C\": \"—•—•\", \"D\": \"—••\", \"E\": \"•\", \"F\": \"••—•\", \"G\": \"——•\", \"H\": \"••••\",\"I\": \"••\", \"J\": \"•———\", \"K\": \"—•—\", \"L\": \"•—••\", \"M\": \"——\", \"N\": \"—•\", \"O\": \"———\", \"P\": \"•——•\", \"Q\": \"——•—\",\"R\": \"•—•\", \"S\": \"•••\", \"T\": \"—\", \"—\": \"—••••—\", \"U\": \"••—\", \"V\": \"•••—\", \"W\": \"•——\", \"X\": \"—••—\", \"Y\": \"—•——\",\"Z\": \"——••\", \"•\": \"•—•—•—\", \"/\": \"—••—•\", \"0\": \"—————\", \"1\": \"•————\", \"2\": \"••———\", \"3\": \"•••——\", \"4\": \"••••—\",\"5\": \"•••••\", \"6\": \"—••••\", \"7\": \"——•••\", \"8\": \"———••\", \"9\": \"————•\", \"?\": \"••——••\"}";
    private final static Map<String,String> MORSE_CODE_MAP = (Map<String, String>) JSON.parse(MORSE_CODE_JSON);
    private final static int ZERO = 0;
    private final static int ONE = 1;

    private static String transform(String[] value, int dense) {
        AtomicReference<String> result = new AtomicReference<>("");
        int j = 0;
        for (String s :
                value) {
            if (Objects.equals(dense, ZERO)) {
                if (!MORSE_CODE_MAP.containsKey(s)) {
                    return null;
                }
                j = 0;
            }
            if (Objects.equals(dense, ONE)) {
                if (!MORSE_CODE_MAP.containsValue(s)) {
                    return null;
                }
                j = 1;
            }

            int finalJ = j;
            MORSE_CODE_MAP.forEach((k, v) -> {
                if(Objects.equals(finalJ,ZERO)){
                    if (Objects.equals(k, s.toUpperCase())) {
                        result.updateAndGet(v1 -> v1 + v + " ");
                    }
                }else{
                    if (Objects.equals(v, s)) {
                        result.updateAndGet(v1 -> v1 + k + " ");
                    }
                }
            });
        }
        return result.toString();
    }

    /*
     *  dense 加密解密标识   0：加密 1：解密
     *  value 密文
     * */
    public static String mainBodyOfTheProgram(String value, int dense) {
        if (value == null) {
            return null;
        }
        String[] strings = value.split(" ");
        return transform(strings, dense);
    }
}
```
# Python版
```python
map = {"A": "•—", "()": "—•——•—", "B": "—•••", "C": "—•—•", "D": "—••", "E": "•", "F": "••—•", "G": "——•", "H": "••••",
       "I": "••", "J": "•———", "K": "—•—", "L": "•—••", "M": "——", "N": "—•", "O": "———", "P": "•——•", "Q": "——•—",
       "R": "•—•", "S": "•••", "T": "—", "—": "—••••—", "U": "••—", "V": "•••—", "W": "•——", "X": "—••—", "Y": "—•——",
       "Z": "——••", "•": "•—•—•—", "/": "—••—•", "0": "—————", "1": "•————", "2": "••———", "3": "•••——", "4": "••••—",
       "5": "•••••", "6": "—••••", "7": "——•••", "8": "———••", "9": "————•", "?": "••——••"}
ZERO = 0
ONE = 1


'''
 value : 密文   dense ：加密标识  0 ：加密  1 ：解密
'''
def mainBodyOfTheProgram(value, dense):
    if value is None:
        return
    objStr = value.split(" ")
    result = ""
    for f in objStr:
        if ZERO == dense:
            if map.get(f) is None:
                return
            result += map.get(f) + " "
        if ONE == dense:
            for key, value in map.items():
                if value == f:
                    result += key + " "
    return result


if __name__ == "__main__":
    print("摩斯密码加密： %s" % mainBodyOfTheProgram("• E", 0))
    print("摩斯密码解密： %s" % mainBodyOfTheProgram(mainBodyOfTheProgram("• E", 0), 1))
```