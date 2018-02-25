package ru.nsu.fit.g13205.kushner;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Konstantin on 14.02.2016.
 */
public class SettingApplication {

    public static final String HELP_INFORMATION = "\"Жизнь Конвея\" – пошаговый алгоритм пересчёта состояния клеток (клеточный автомат). На каждом этапе\n"+
            "(году) жизни клетки могут рождаться (мёртвые клетки становятся живыми) и умирать (живые клетки стано-\n" +
            "вятся мёртвыми), а также клетка может не измениться, то есть остаться мёртвой или живой, какой она была\n" + "" +
            "на предыдущем шаге. Какой будет клетка на следующем этапе, определяется для каждой клетки (в отдельно-\n" + "" +
            "сти!) количеством живых соседей на предыдущем этапе и их \\\"влиянием\\\")\n" +
            "\n" +
            "Интерфейс:\n" +
            "1)Кнопка Run - запускает алгоритм(за каждую секкунду проходит год)\n" +
            "2)Кнопка Step - пошаговое выполнение(при однократном нажатии проходит год)\n" +
            "3)Кнопка Stop - останавливает алгоритм\n" +
            "3)В EditSetting - можно настраивать размер ячейки, ширину линии, размер поля, mode\n" +
            "4)В GameSetting - можно настраивать значения LIFE_BEGIN, LIVE_END, BIRTH_BEGIN, BIRTH_END, FST_IMPACT, SND_IMPACT\n" +
            "5)Mode:\n" +
            "-XOR - была живая/мертвая, после нажатия стала мертвой/живой соответственно.\n" +
            "-REPLACE - была мертвой, после нажатия стала живой. После этого не будет реагировать на нажатия\n" +
            "6)Show/hide impact value - показывает/скрывает состояние ячейки"+
            "7)Имеется возможность созранять, создават, открывать, файл";

    public static final String ABOUT_MESSAGE = "© Kushner Konstantin, NSU FIT 13205, 2016";

    public static final int M_DEFAULT_VALUE = 6;
    public static final int N_DEFAULT_VALUE = 6;
    public static final int CELL_DEFAULT_SIZE = 50;
    public static final int LINE_DEFAULT_WIDTH = 1;
    public static final int DEFAULT_MODE = 1;

    public static final int MAX_FIELD_SIZE = 50;

    public static final double LIFE_BEGIN = 2.0;
    public static final double LIFE_END = 3.3;
    public static final double BIRTH_BEGIN = 2.3;
    public static final double BIRTH_END = 2.9;
    public static final double FST_IMPACT = 1.0;
    public static final double SND_IMPACT = 0.3;

    public static final int FONT_SIZE = 15;
    public static final int FONT_WIDTH = 3;

    public static String EXTENSION = "txt";
    public static Color UN_CLICKED_COLOR = new Color(0, 112, 255);
    public static Color CLICKED_COLOR = new Color(253, 255, 15);
    public static Color BORDER_COLOR =  new Color(0,0,0);
    public static Color FONT_COLOR = new Color(255, 255, 255);
    public static Color IMPACT_COLOR = new Color(255, 10, 0);
    public static Font IMPACT_FONT = new Font("Lucida Sans Typewriter", Font.BOLD, FONT_SIZE);


    public static Color ANOTHER_COLOR = new Color(255, 76, 0);
}
