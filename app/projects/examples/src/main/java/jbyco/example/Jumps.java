package jbyco.example;

/**
 * Created by vendy on 6.5.16.
 */
public class Jumps {

    int function(int a, int b, boolean c) {

        if (a == b) {
            if (c == true) {
                return a;
            }
        }
        else if (a < b) {
            return b;
        }
        else {
            return 0;
        }

        return -1;
    }
}
