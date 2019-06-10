package com.integra.demo_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RoomDBMainActivity extends AppCompatActivity {
    int n = 10000000;
    EditText editTextname, editTextdesc, editTextdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActivityMainBinding  binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setContentView(R.layout.activity_main);
        editTextname = (EditText) findViewById(R.id.name);
        editTextdesc = (EditText) findViewById(R.id.desc);
        editTextdetails = (EditText) findViewById(R.id.details);
        Button addBtn = (Button) findViewById(R.id.addBtn);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addroomData();
            }
        });

        //   numTOwords((n/1000000000),"hundred");
        // numTOwords((n/10000000)%100,"crore");
        //  numTOwords((n/100000)%100,"lakh");
        // numTOwords((n/1000)%100,"thousand");
        //numTOwords((n/100)%10,"hundred");
        // numTOwords((n%100)," ");
        // stringReverse("Name");
        //threecrossThreearray();
        //getMissingNumber();
        // reverseNumber(123456);
        // System.out.println("reverseNumber --->"+reverseNumber(123456));
//        for (int i = 1; i <= 12; i++) {
//            System.out.println("fibinocciNumberadd -->" + fibinocciNumberadd(i) + " ");
//        }
        // myThread();
        // mythreadExtends();
        // mymultiplethread();
//        try {
//            syncThread();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // MySingleTonClass mySingleTonClass = MySingleTonClass.getInstance();
        // System.out.println("mySingleTonClass --->"+mySingleTonClass);
        // strrever();
        // countwordsfromstr();
        //hashmapwithIterator();
        //primeRnot();
        // palindrome();
//        for (int i =0;i<9;i++){
//            fabinooci(i);
//            System.out.println("fibonoci===>"+fabinooci(i));
//        }
        //    fibinocizer();
        // advancedLoop();
        duplicates();
        //largestArr();
        // armstrong();
        // strinbuff();
        //duplicatlist();
        randomnumber();
    }


    private void addroomData() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTask(editTextname.getText().toString());
        taskModel.setDesc(editTextdesc.getText().toString());
        taskModel.setDetails(editTextdetails.getText().toString());
        new dataBaseData(taskModel).execute();
    }

    private void randomnumber() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            System.out.println("random number " + random.nextInt());
        }
    }

    private void duplicatlist() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("a");
        list.add("d");
        System.out.println("list " + list);
        HashSet<String> strings = new HashSet<>(list);
        System.out.println("HashSet  duplicat" + strings);
        ArrayList<String> strings1 = new ArrayList<>(strings);
        System.out.println("without duplicat" + strings1);
    }

    private void strinbuff() {
        String s = " we are   on    development   ";
        StringBuffer buffer = new StringBuffer();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ' ' && chars[i] != '\t') {
                buffer.append(chars[i]);
            }
        }
        System.out.println("o -->" + buffer.toString());

        String s1 = s.replaceAll("\\s", "");
        System.out.println("v -->" + s1);

    }

    private void armstrong() {
        int c = 0, a, temp, n = 153;
        temp = n;

        while (n > 0) {
            a = n % 10;
            n = n / 10;
            c = c + (a * a * a);
        }
        if (temp == c) {
            System.out.println("Arms" + temp);
        } else {
            System.out.println("no " + temp);
        }
    }

    private void largestArr() {
        int[] intarray = {5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 9};
        int l = intarray[0];
        int sl = intarray[1];
        for (int i = 0; i < intarray.length; i++) {
            if (intarray[i] > l) {
                sl = l;
                l = intarray[i];
            } else if (intarray[i] > sl && intarray[i] != l) {
                sl = intarray[i];
            }
        }
        System.out.println("-->" + sl + "==>" + l);
    }

    private void duplicates() {
        int count = 0;
        String s = "wellcommel";
        char[] c = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            for (int j = i + 1; j < s.length(); j++) {
                if (c[i] == c[j]) {
                    System.out.println("duplicate " + c[j]);
                    count++;
                    break;
                }
            }
        }
    }

    private void advancedLoop() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("a");
        strings.add("b");
        strings.add("c");
        Iterator iterator = strings.iterator();
        while (iterator.hasNext()) {
            System.out.println("while loop list " + iterator.next());
        }
        for (String o : strings) {
            System.out.println(" advanced for loop list " + o);
        }
        for (int i = 0; i < strings.size(); i++) {
            System.out.println("for loop " + strings.get(i));
        }
    }

    private void fibinocizer() {
        int num = 9, a = 0, b = 0, c = 1;
        for (int i = 0; i <= num; i++) {
            a = b;
            b = c;
            c = a + b;
            System.out.println("num --->" + a + "b-->" + b + "c-->" + c);
        }
    }

    private int fabinooci(int num) {
        if (num == 1 || num == 2) {
            return 1;
        }
        int fibo1 = 1, fibo2 = 1, fibonacci = 1;
        for (int i = 3; i <= num; i++) {

            //Fibonacci number is sum of previous two Fibonacci number
            fibonacci = fibo1 + fibo2;
            fibo1 = fibo2;
            fibo2 = fibonacci;

        }
        return fibonacci; //Fibonacci number
//        if (num == 0 || num == 2) {
//            return 1;
//        }
//        for (int i =3;i<=num;i++){
//            fibonacci = fibo1+fibo2;
//            fibo1 = fibo2;
//            fibo2 = fibonacci;
//
//        }
//
//        return fibonacci;
    }

    private void palindrome() {
        String s = "mam", reverse = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            reverse = reverse + s.charAt(i);
        }
        if (s.equals(reverse)) {
            System.out.println(s + " same " + reverse);
        } else {
            System.out.println(s + " not same " + reverse);
        }
    }

    private void primeRnot() {
        int temp, num = 50;
        boolean isboolean = true;
        for (int i = 2; i <= num / 2; i++) {
            temp = num % i;
            if (temp == 0) {
                isboolean = false;
                break;
            }
        }
        if (isboolean) {
            System.out.println("Given no  is prime ");
        } else {
            System.out.println("Given no  is not prime ");
        }
    }

    private void hashmapwithIterator() {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(12, "w");
        map.put(13, "e");
        map.put(14, "r");
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            System.out.println("while lopp " + entry.getKey() + " -- >" + entry.getValue());
        }
        for (Map.Entry entry : map.entrySet()) {
            System.out.println("for lopp " + entry.getKey() + " -- >" + entry.getValue());
        }
    }

    private void countwordsfromstr() {
        String word = "this is repeat this is not repeated word";
        String[] wordsplit = word.split(" ");
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < wordsplit.length - 1; i++) {
            if (hashMap.containsKey(wordsplit[i])) {
                int count = hashMap.get(wordsplit[i]);
                hashMap.put(wordsplit[i], count + 1);
            } else {
                hashMap.put(wordsplit[i], 1);
            }
        }
        System.out.println("hashmap value --->" + hashMap);
    }

    private void strrever() {
        String name = "reverse";
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.reverse();
        System.out.println("string builder -->" + sb);
        StringBuilder sb1 = new StringBuilder();
        for (int i = name.length() - 1; i >= 0; i--) {
            sb1.append(name.charAt(i));
        }
        System.out.println("string builder ==>" + sb1);
        int x = 5;
        int y = 3;
        x = x + y;
        y = x - y;
        x = x - y;
        System.out.println("swap value ==>" + x + y);
    }

    private void syncThread() throws InterruptedException {
        Object o = new Object();
        Object o1 = new Object();
        Object o2 = new Object();
        Thread thread = new Thread(new SyncThreadclas(o, o1), "t1");
        Thread thread1 = new Thread(new SyncThreadclas(o1, o2), "t2");
        Thread thread2 = new Thread(new SyncThreadclas(o2, o), "t3");
        thread.start();
        Thread.sleep(5000);
        thread1.start();
        Thread.sleep(5000);
        thread2.start();
    }

    private void mymultiplethread() {
        new Mymultithread("one");
        new Mymultithread("two");
        new Mymultithread("three");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }
        System.out.println("Main thread exiting.");
    }

    private void mythreadExtends() {
        class Myclass1 extends Thread {
            @Override
            public void run() {
                super.run();
                System.out.println("extending thread class ");
            }
        }
        Myclass1 myclass = new Myclass1();
        myclass.start();
    }

    private void myThread() {
        class myclass implements Runnable {
            @Override
            public void run() {
                System.out.println("my class thread is running....");
            }
        }
        Thread thread = new Thread(new myclass());
        thread.start();
    }

    private int fibonacci2(int number) {
        if (number == 1 || number == 2) {
            return 1;
        }
        int fibo1 = 1, fibo2 = 1, fibonacci = 1;
        for (int i = 3; i <= number; i++) {

            //Fibonacci number is sum of previous two Fibonacci number
            fibonacci = fibo1 + fibo2;
            fibo1 = fibo2;
            fibo2 = fibonacci;

        }
        return fibonacci; //Fibonacci number

    }

    private int fibinocciNumberadd(int no) {
        if (no == 1 || no == 2) {
            return 1;
        }
        int fibo1 = 1, fibo2 = 1, fibinoci = 1;
        for (int i = 3; i <= no; i++) {
            fibinoci = fibo1 + fibo2;
            fibo1 = fibo2;
            fibo2 = fibinoci;
        }
        return fibinoci;
    }

    private int reverseNumber(int number) {
        int reverse = 0;
        int remainder = 0;
        do {
            remainder = number % 10;
            reverse = reverse * 10 + remainder;
            number = number / 10;
            System.out.println("remainder --->" + remainder);
            System.out.println("reverse --->" + reverse);
            System.out.println("number --->" + number);
        } while (number > 0);
        System.out.println("reverse --->" + reverse);
        return reverse;
    }

    private void getMissingNumber() {
        int total_num;
        int[] numbers = new int[]{1, 2, 3, 4, 5, 6, 7, 9};
        total_num = 9;
        int expected_num_sum = total_num * ((total_num + 1) / 2);
        int num_sum = 0;
        for (int i : numbers) {
            num_sum += i;
            System.out.println("missing i value ==>" + i + "num_sum ==>" + num_sum);
            //  System.out.println("missing num value ==>"+(num_sum += i));
        }
        System.out.println("missing ==>" + (expected_num_sum - num_sum));

    }

    private void threecrossThreearray() {

        int name[][][] = {{{1, 2}, {3, 4}},
                {{5, 6}, {7, 8}}};
        for (int i = 0; i < name.length; i++) {
            for (int j = 0; j < name.length; j++) {
                for (int k = 0; k < name.length; k++) {
                    System.out.println("array position == >i " + i + "j " + j + " k" + k);
                }
//                System.out.println("array position == >i "+i+"j "+j+" k"+k);
            }
//            System.out.println("array position == >i "+i+"j "+j+" k"+k);
        }

    }

    private void stringReverse(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = name.length() - 1; i >= 0; i--) {
            stringBuilder.append(name.charAt(i));
        }
        System.out.println("stringBuilder ==>" + stringBuilder);
    }

    private void numTOwords(int n, String ch) {
        String[] one = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
                "eleven", "twele", "thirteen", "fourteen",
                "fifthtenn", "sixteen", "seventeen", "eightenn", "nineteen"};
        String[] two = {"", "", "twenty", "thirty", "fourty", "fifty", "sixty", "seventy", "eighty", "ninty"};

        if (n <= 0) {
            System.out.println("given number less than zero " + n);
        } else {
            if (n > 19) {
                System.out.println(two[n / 10] + " " + one[n % 10]);
            } else {
                System.out.println(one[n]);
            }
            if (n > 0) {
                System.out.println(ch);
            }
        }


    }

    class dataBaseData extends AsyncTask<String, Void, String> {

        private TaskModel taskModel;
        private String result = "failure";

        public dataBaseData(TaskModel taskModel) {
            this.taskModel = taskModel;
        }

        @Override
        protected String doInBackground(String... strings) {
            MySingleTonClass.getInstance(getApplicationContext())
                    .getAppDataBase()
                    .taskDao()
                    .insert(taskModel);
            result = "success";
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new getroomData().execute();
        }

    }

    private class getroomData extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            List<TaskModel> getTaskModels = MySingleTonClass.getInstance(getApplicationContext())
                    .getAppDataBase()
                    .taskDao()
                    .getAllTasks();
            System.out.println(getTaskModels.size() + "getTaskModels ==>" + getTaskModels.get(0).getTask()
                    + "\n" + getTaskModels.get(0).getDesc()
                    + "\n" + getTaskModels.get(0).getDetails());
            return null;
        }


    }

    class Mymultithread implements Runnable {
        String name;
        Thread t;

        Mymultithread(String s) {
            name = s;
            t = new Thread(this, s);
            System.out.println("name --->" + s);
            t.start();
        }

        @Override
        public void run() {
            for (int i = 5; i > 0; i--) {
                try {
                    System.out.println("name --->" + name + "   i value -->" + i);
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(name + "InterruptedException");
                }
                System.out.println(name + " exiting.");
            }
        }
    }

    private class SyncThreadclas implements Runnable {
        Object object;
        Object object1;

        public SyncThreadclas(Object o, Object o1) {
            this.object = o;
            this.object1 = o1;
        }

        @Override
        public void run() {
            String s = Thread.currentThread().getName();
            System.out.println(s + " acquiring lock on " + object);
            synchronized (object) {
                System.out.println(s + "acquired  lock on" + object);
                work();
                System.out.println(s + "acquiring  lock on " + object1);
                synchronized (object1) {
                    System.out.println(s + "acquired lock on" + object1);
                    work();
                    // System.out.println(s + "acquiring thread lock on " + object1);
                }
                System.out.println(s + "released thread lock " + object1);
            }
            System.out.println(s + "released thread lock " + object);
            System.out.println(s + "existing thread " + object);
        }

        private void work() {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
