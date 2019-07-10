package REST;

import spark.servlet.SparkApplication;

public class Main {
    public static void main(String[] args) {
        SparkApplication app = new App();

        app.init();
    }
}
