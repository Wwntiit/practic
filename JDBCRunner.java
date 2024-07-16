

import java.sql.*;
import java.util.Scanner;

public class JDBCRunner {

    private static final String PROTOCOL = "jdbc:postgresql://";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL_LOCALE_NAME = "localhost/";
    private static final String URL_REMOTE = "10.242.65.114:5432/";

    private static final String DATABASE_NAME = "mobile_legends";
    public static final String USER_NAME = "postgres";
    public static final String DATABASE_PASS = "postgres";

    public static final String DATABASE_URL = PROTOCOL + URL_LOCALE_NAME + DATABASE_NAME;


    public static void main(String[] args) {


        checkDriver();
        checkDB();

        System.out.println("Подключение к базе данных | " + DATABASE_URL + "\n");
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS)) {
            Scanner scanner = new Scanner(System.in);
            String command;

            while (true) {
                System.out.println("Введите команду (или 'exit' для выхода):");
                command = scanner.nextLine();

                if (command.equalsIgnoreCase("exit")) {
                    break;
                }

                switch (command.toLowerCase()) {
                    case "getheroes":
                        getHeroes(connection);
                        break;
                    case "getskins":
                        getSkins(connection);
                        break;
                    case "getweapon":
                        getWeapon(connection);
                        break;
                    case "getnames":
                        System.out.println("Введите тип героя:");
                        String type = scanner.nextLine();
                        getNames(connection, type);
                        break;
                    case "getnamesskin":
                        System.out.println("Введите редкость скина:");
                        String rarity = scanner.nextLine();
                        getNamesSkin(connection, rarity);
                        break;
                    case "correctweapon":
                        System.out.println("Введите id, имя и id класса через запятую:");
                        String[] weaponData = scanner.nextLine().split(",");
                        correctWeapon(connection, Integer.parseInt(weaponData[0]), weaponData[1], Integer.parseInt(weaponData[2]));
                        break;
                    case "correcthero":
                        System.out.println("Введите id, имя, стоимость и тип через запятую:");
                        String[] heroData = scanner.nextLine().split(",");
                        correctHero(connection, Integer.parseInt(heroData[0]), heroData[1], Integer.parseInt(heroData[2]), heroData[3]);
                        break;
                    case "correctskin":
                        System.out.println("Введите id, имя, стоимость, редкость и id героя через запятую:");
                        String[] skinData = scanner.nextLine().split(",");
                        correctSkin(connection, Integer.parseInt(skinData[0]), skinData[1], Integer.parseInt(skinData[2]), skinData[3], Integer.parseInt(skinData[4]));
                        break;
                    case "removehero":
                        System.out.println("Введите id героя для удаления:");
                        int heroId = Integer.parseInt(scanner.nextLine());
                        removeHero(connection, heroId);
                        break;
                    case "addhero":
                        System.out.println("Введите имя, стоимость и тип через запятую для добавления:");
                        String[] newHeroData = scanner.nextLine().split(",");
                        addHero(connection, newHeroData[0], Integer.parseInt(newHeroData[1]), newHeroData[2]);
                        break;
                    default:
                        System.out.println("Неизвестная команда.");
                        break;
                }
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                System.out.println("Произошло дублирование данных");
            } else throw new RuntimeException(e);



           //            correctWeapon(connection,1,"клинок семи морей",10 );
//            removeHero(connection,13 );//            getWeapon(connection);
//            getNamesSkin(connection, "legend");

//            getNames(connection, "стрелок");
//          correctSkin(connection,1, "миссис расвет", 4000, "legend", 10);
//            getHeroes(connection);
//            correctHero(connection, 5,"нолан",32000, "убийца" );

            //  getNames(connection,"name", "танк" );
//            removeHero(connection, 12);

        }

    }


    public static void checkDriver() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Нет JDBC-драйвера! Подключите JDBC-драйвер к проекту согласно инструкции.");
            throw new RuntimeException(e);
        }
    }


    public static void checkDB() {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS);
        } catch (SQLException e) {
            System.out.println("Нет базы данных! Проверьте имя базы, путь к базе или разверните локально резервную копию согласно инструкции");
            throw new RuntimeException(e);
        }
    }


    private static void getHeroes(Connection connection) throws SQLException {

        String columnName0 = "id", columnName1 = "name", columnName2 = "cost", columnName3 = "type";

        int param0, param2;
        String param1, param3;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM hero;");

        while (rs.next()) {
            param3 = rs.getString(columnName3);
            param2 = rs.getInt(columnName2);
            param1 = rs.getString(columnName1);
            param0 = rs.getInt(columnName0);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3);
        }
    }

    private static void getSkins(Connection connection) throws SQLException {
        // имена столбцов
        String columnName0 = "id", columnName1 = "name", columnName2 = "cost", columnName3 = "rarity", columnName4 = "hero_id";
        // значения ячеек
        int param0, param2, param4;
        String param1, param3;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM skin;");

        while (rs.next()) {
            param4 = rs.getInt(columnName4);
            param3 = rs.getString(columnName3);
            param2 = rs.getInt(columnName2);
            param1 = rs.getString(columnName1);
            param0 = rs.getInt(columnName0);

            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4);
        }
    }private static void getWeapon(Connection connection) throws SQLException {
        String columnName0 = "id", columnName1 = "name", columnName2 = "class_id";
        int param0, param2;
        String param1;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM weapon;");

        while (rs.next()) {
            param2 = rs.getInt(columnName2);
            param1 = rs.getString(columnName1);
            param0 = rs.getInt(columnName0);

            System.out.println(param0 + " | " + param1 + " | " + param2);
        }
    }


    private static void getNames(Connection connection, String type) throws SQLException {
        String query = "SELECT name FROM hero WHERE type=?";
        PreparedStatement statement1 = connection.prepareStatement(query);
        statement1.setString(1, type);
        ResultSet resultSet = statement1.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString("name"));
        }
    }

    private static void getNamesSkin(Connection connection, String type) throws SQLException {
        String query = "SELECT name FROM skin WHERE rarity=?";
        PreparedStatement statement1 = connection.prepareStatement(query);
        statement1.setString(1, type);
        ResultSet resultSet = statement1.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString("name"));
        }


    }
    private static void correctWeapon(Connection connection, int id, String name, int class_id) throws
            SQLException {
        if (id < 0 || name.isBlank() || class_id < 0 ) return;

        PreparedStatement statement = connection.prepareStatement("UPDATE weapon SET name=?, class_id=? WHERE id=?;");
        statement.setString(1, name);
        statement.setInt(2, class_id);
        statement.setInt(3, id);

        int count = statement.executeUpdate();

        System.out.println("UPDATEd " + count + " name");
        getWeapon(connection);
    }




    private static void correctHero(Connection connection, int id, String name, int cost, String type) throws
            SQLException {
        if (id < 0 || name.isBlank() || cost < 0 || type.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement("UPDATE hero SET name=?, cost=?, type=?  WHERE id=?;");
        statement.setString(1, name);
        statement.setInt(2, cost);
        statement.setString(3, type);
        statement.setInt(4, id);

        int count = statement.executeUpdate();

        System.out.println("UPDATEd " + count + " name");
        getHeroes(connection);
    }

    private static void correctSkin(Connection connection, int id, String name, int cost, String rarity, int hero_id) throws
            SQLException {
        if (id < 0 || name.isBlank()|| cost < 0 || rarity.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement("UPDATE skin SET name=?, cost=?, rarity=?, hero_id=?  WHERE id=?;");
        statement.setString(1, name);
        statement.setInt(2, cost);
        statement.setString(3, rarity);
        statement.setInt(4, hero_id);
        statement.setInt(5, id);
        int count = statement.executeUpdate();

        System.out.println("UPDATEd " + " | " + count + " | " + "name" + " | " + cost + " | " + "rarity" + " | " + hero_id);
        getSkins(connection);

    }

    private static void removeHero(Connection connection, int id) throws SQLException {
        if (id < 0) return;

        PreparedStatement statement = connection.prepareStatement("DELETE from hero WHERE id=?;");
        statement.setInt(1, id);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " heroes");
        getHeroes(connection);
    }

    private static void addHero(Connection connection, String name, int cost, String type) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO hero(name, cost, type) VALUES (?, ?, ?) returning id;", Statement.RETURN_GENERATED_KEYS);    // создаем оператор шаблонного-запроса с "включаемыми" параметрами - ?
        statement.setString(1, name);
        statement.setInt(2, cost);
        statement.setString(3, type);


        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор героя " + rs.getInt(1));
        }

        System.out.println("INSERTed " + count + "hero");
        getHeroes(connection);
    }


}