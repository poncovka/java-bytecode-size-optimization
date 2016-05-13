package jbyco.basic_examples;

/**
 * Created by vendy on 3.5.16.
 */
public class FinallyBlock {

    public void function() {

        try {
            System.out.println("try");
            int a = 1 / 0;
        } catch (RuntimeException e) {
            System.out.println("catch");
        } finally {
            System.out.println("finally");
        }

    }
}
