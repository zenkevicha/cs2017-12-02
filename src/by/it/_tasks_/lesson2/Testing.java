package by.it._tasks_.lesson2;

import org.junit.Test;

import java.io.*;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

@SuppressWarnings("all") //море warnings. всех прячем.

//поставьте курсор на следующую строку и нажмите Ctrl+Shift+F10
public class Testing {


    @Test(timeout = 500)
    public void testTaskA1() throws Exception {
        Testing testing = new Testing(TaskA1.class);
        testing.contains("Hello world!");
    }

    @Test(timeout = 500)
    public void testTaskA2() throws Exception {
        Testing testing = new Testing(TaskA2.class);
        testing.contains(
                "Я начинаю изучать Java!\n" +
                        "Я начинаю изучать Java!\n" +
                        "Я начинаю изучать Java!\n" +
                        "Я начинаю изучать Java!\n" +
                        "Я начинаю изучать Java!\n"
        );
    }

    @Test(timeout = 500)
    public void testTaskA3() throws Exception {
        Testing testing = new Testing(TaskA3.class);
        testing.contains("3*3+4*4=25");
    }

    @Test(timeout = 500)
    public void testTaskB1() throws Exception {
        Testing testing = new Testing(TaskB1.class, "7");
        testing.contains("49");
    }

    @Test(timeout = 500)
    public void testTaskB2() throws Exception {
        Testing testing = new Testing(TaskB2.class);
        testing.contains("20");
    }

    @Test(timeout = 500)
    public void testTaskB3() throws Exception {
        Testing testing = new Testing(TaskB3.class);
        testing.contains("C Новым Годом");
    }

    @Test(timeout = 500)
    public void testTaskC1() throws Exception {
        Testing testing = new Testing(TaskC1.class, "7\n3\n");
        testing.contains("Sum = 10\n");
    }

    @Test(timeout = 500)
    public void testTaskC2() throws Exception {
        Testing testing = new Testing(TaskC2.class, "34\n26\n");
        testing.contains(
                "DEC:34+26=60\n" +
                        "BIN:100010+11010=111100\n" +
                        "HEX:22+1a=3c\n" +
                        "OKT:42+32=74\n");
    }

    @Test(timeout = 500)

    public void testTaskC3() throws Exception {
        Testing testing = new Testing(TaskC3.class, "75\n");
        testing.contains("29.51\n");
        testing = new Testing(TaskC3.class, "100\n");
        testing.contains("39.35\n");
        try {
            Method m = TaskC3.class.getDeclaredMethod("getWeight", int.class);
            assertEquals((Double) m.invoke(null, 100), 39.35, 1e-100);
            assertEquals((Double) m.invoke(null, 75), 29.51, 1e-100);
        } catch (NoSuchMethodException e) {
            org.junit.Assert.fail("Метод getWeight не найден");
        }
    }

    /*
    ===========================================================================================================
    НИЖЕ ВСПОМОГАТЕЛЬНЫЙ КОД ТЕСТОВ. НЕ МЕНЯЙТЕ В ЭТОМ ФАЙЛЕ НИЧЕГО.
    Но изучить как он работает - можно, это всегда будет полезно.
    ===========================================================================================================
     */
    public Testing() {
        //Конструктор тестов
    }

    //Конструктор тестов
    private Testing(Class<?> c) {
        this(c, "");
    }

    //Конструктор тестов
    private Testing(Class<?> c, String in) {
        reader = new StringReader(in); //заполнение ввода
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return reader.read();
            }
        };
        System.setIn(inputStream);   //перехват ввода

        System.setOut(newOut); //перехват стандартного вывода
        try {
            Class[] argTypes = new Class[]{String[].class};
            Method main = c.getDeclaredMethod("main", argTypes);
            main.invoke(null, (Object) new String[]{});

        } catch (Exception x) {
            x.printStackTrace();
        }
        System.setOut(oldOut); //возврат вывода
    }

    //проверка вывода
    private void contains(String str) {
        assertTrue("Строка не найдена: " + str + "\n", stringWriter.toString().contains(str));
    }


    //переменные теста
    private StringWriter stringWriter = new StringWriter();
    private PrintStream oldOut = System.out;
    private StringReader reader;


    //поле для перехвата потока вывода
    private PrintStream newOut;

    {
        newOut = new PrintStream(new OutputStream() {
            private byte bytes[] = new byte[2];

            @Override
            public void write(int b) throws IOException {
                if (b < 0) { //ловим и собираем двухбайтовый UTF (первый код > 127, или в байте <0)
                    if (bytes[0] == 0) { //если это первый байт
                        bytes[0] = (byte) b; //то запомним его
                    } else {
                        bytes[1] = (byte) b; //иначе это второй байт
                        String s = new String(bytes); //соберем весь символ
                        stringWriter.append(s); //запомним вывод для теста
                        oldOut.append(s); //копию в обычный вывод
                        bytes[0] = 0; //забудем все.
                    }
                } else {
                    char c = (char) b; //ловим и собираем однобайтовый UTF
                    bytes[0] = 0;
                    if (c != '\r') {
                        stringWriter.write(c); //запомним вывод для теста
                    }
                    oldOut.write(c); //копию в обычный вывод
                }
            }
        });
    }

}
