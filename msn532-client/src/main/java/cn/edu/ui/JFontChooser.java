package cn.edu.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 字体选择器
 *
 * @author zhouyongyi 2014-12-20 引入源码
 */
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class JFontChooser extends JPanel {

    // 设置界面风格
    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager
                .getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // [start] 定义变量
    private String current_fontName = "宋体";// 当前的字体名称,默认宋体.
    private String showStr = "AaBb,CcDd.";// 展示的文字
    private int current_fontStyle = Font.PLAIN;// 当前的字样,默认常规.
    private int current_fontSize = 9;// 当前字体大小,默认9号.
    private Color current_color = Color.BLACK;// 当前字色,默认黑色.
    private JDialog dialog; // 用于显示模态的窗体
    private JLabel lblFont; // 选择字体的LBL
    private JLabel lblStyle; // 选择字型的LBL
    private JLabel lblSize; // 选择字大小的LBL
    private JLabel lblColor; // 选择Color的label
    private JLabel otherColor; // 其它颜色
    private JTextField txtFont; // 显示选择字体的TEXT
    private JTextField txtStyle; // 显示选择字型的TEXT
    private JTextField txtSize; // 显示选择字大小的TEXT
    private JTextField showTF; // 展示框（输入框）
    private JList lstFont; // 选择字体的列表.
    private JList lstStyle; // 选择字型的列表.
    private JList lstSize; // 选择字体大小的列表.
    private JComboBox cbColor; // 选择Color的下拉框.
    private JButton ok, cancel; // "确定","取消"按钮.
    private JScrollPane spFont;
    private JScrollPane spSize;
    private JPanel showPan; // 显示框.
    private Map<String, Integer> sizeMap; // 字号映射表.
    private Map colorMap; // 字着色映射表.
    private Font selectedfont; // 用户选择的字体
    private Color selectedcolor; // 用户选择的颜色

    // [end]

    // 无参初始化
    public JFontChooser() {
        this.selectedfont = null;
        this.selectedcolor = null;
        /* 初始化界面 */
        init(null, null);
    }

    // 重载构造，有参的初始化 用于初始化字体界面
    public JFontChooser(Font font, Color color) {
        if (font != null) {
            this.selectedfont = font;
            this.selectedcolor = color;
            this.current_fontName = font.getName();
            this.current_fontSize = font.getSize();
            this.current_fontStyle = font.getStyle();
            this.current_color = color;
            /* 初始化界面 */
            init(font, color);
        } else {
            JOptionPane.showMessageDialog(this, "没有被选择的控件", "错误",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // 可供外部调用的方法
    public Font getSelectedfont() {
        return selectedfont;
    }

    public void setSelectedfont(Font selectedfont) {
        this.selectedfont = selectedfont;
    }

    public Color getSelectedcolor() {
        return selectedcolor;
    }

    public void setSelectedcolor(Color selectedcolor) {
        this.selectedcolor = selectedcolor;
    }

    /* 初始化界面 */
    // private void init(Font txt_font) {
    private void init(Font font, Color color) {
        // 实例化变量
        lblFont = new JLabel("字体:");
        lblStyle = new JLabel("字型:");
        lblSize = new JLabel("大小:");
        lblColor = new JLabel("颜色:");
        otherColor = new JLabel("<html><U>其它颜色</U></html>");
        txtFont = new JTextField("宋体");
        txtStyle = new JTextField("常规");
        txtSize = new JTextField("9");

        // 取得当前环境可用字体.
        GraphicsEnvironment ge = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();

        lstFont = new JList(fontNames);

        // 字形.
        lstStyle = new JList(new String[]{"常规", "粗休", "斜休", "粗斜休"});

        // 字号.
        String[] sizeStr = new String[]{"8", "9", "10", "11", "12", "14",
            "16", "18", "20", "22", "24", "26", "28", "36", "48", "72",
            "初号", "小初", "一号", "小一", "二号", "小二", "三号", "小三", "四号", "小四",
            "五号", "小五", "六号", "小六", "七号", "八号"};
        int sizeVal[] = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36,
            48, 72, 42, 36, 26, 24, 22, 18, 16, 15, 14, 12, 11, 9, 8, 7, 6,
            5};
        sizeMap = new HashMap<String, Integer>();
        for (int i = 0; i < sizeStr.length; ++i) {
            sizeMap.put(sizeStr[i], sizeVal[i]);
        }
        lstSize = new JList(sizeStr);
        spFont = new JScrollPane(lstFont);
        spSize = new JScrollPane(lstSize);

        // 颜色
        String[] colorStr = new String[]{"黑色", "蓝色", "青色", "深灰", "灰色", "绿色",
            "浅灰", "洋红", "桔黄", "粉红", "红色", "白色", "黄色"};
        Color[] colorVal = new Color[]{Color.BLACK, Color.BLUE, Color.CYAN,
            Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY,
            Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED,
            Color.WHITE, Color.YELLOW};
        colorMap = new HashMap();
        for (int i = 0; i < colorStr.length; i++) {
            colorMap.put(colorStr[i], colorVal[i]);
        }
        cbColor = new JComboBox(colorStr);
        showPan = new JPanel();
        ok = new JButton("确定");
        cancel = new JButton("取消");

        // 布局控件
        // 字体框
        this.setLayout(null); // 不用布局管理器
        add(lblFont);
        lblFont.setBounds(12, 10, 30, 20);
        txtFont.setEditable(false);
        add(txtFont);
        txtFont.setBounds(10, 30, 155, 20);
        txtFont.setText("宋体");
        lstFont.setSelectedValue("宋体", true);
        if (font != null) {
            txtFont.setText(font.getName());
            lstFont.setSelectedValue(font.getName(), true);
        }

        add(spFont);
        spFont.setBounds(10, 50, 155, 100);

        // 样式
        add(lblStyle);
        lblStyle.setBounds(175, 10, 30, 20);
        txtStyle.setEditable(false);
        add(txtStyle);
        txtStyle.setBounds(175, 30, 130, 20);
        lstStyle.setBorder(javax.swing.BorderFactory
            .createLineBorder(Color.gray));
        add(lstStyle);
        lstStyle.setBounds(175, 50, 130, 100);
        txtStyle.setText("常规"); // 初始化为默认的样式
        lstStyle.setSelectedValue("常规", true); // 初始化为默认的样式
        if (font != null) {
            lstStyle.setSelectedIndex(font.getStyle()); // 初始化样式list
            if (font.getStyle() == 0) {
                txtStyle.setText("常规");
            } else if (font.getStyle() == 1) {
                txtStyle.setText("粗体");
            } else if (font.getStyle() == 2) {
                txtStyle.setText("斜体");
            } else if (font.getStyle() == 3) {
                txtStyle.setText("粗斜体");
            }
        }

        // 大小
        add(lblSize);
        lblSize.setBounds(320, 10, 30, 20);
        txtSize.setEditable(false);
        add(txtSize);
        txtSize.setBounds(320, 30, 60, 20);
        add(spSize);
        spSize.setBounds(320, 50, 60, 100);
        lstSize.setSelectedValue("9", false);
        txtSize.setText("9");
        if (font != null) {
            lstSize.setSelectedValue(Integer.toString(font.getSize()), false);
            txtSize.setText(Integer.toString(font.getSize()));
        }

        // 颜色
        add(lblColor);
        lblColor.setBounds(15, 230, 30, 20);
        cbColor.setBounds(15, 255, 100, 22);
        cbColor.setMaximumRowCount(5);
        add(cbColor);
        otherColor.setForeground(Color.blue);
        otherColor.setBounds(127, 255, 60, 22);
        otherColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(otherColor);

        // 展示框
        showTF = new JTextField();
        showTF.setFont(new Font(current_fontName, current_fontStyle,
            current_fontSize));
        showTF.setBounds(10, 10, 300, 50);
        showTF.setHorizontalAlignment(JTextField.CENTER);
        showTF.setText(showStr);
        showTF.setBackground(Color.white);
        showTF.setEditable(false);
        showPan.setBorder(javax.swing.BorderFactory.createTitledBorder("示例"));
        add(showPan);
        showPan.setBounds(10, 160, 370, 80);
        showPan.setLayout(new BorderLayout());
        showPan.add(showTF);
        if (font != null) {
            showTF.setFont(font); // 设置示例中的文字格式
        }
        if (font != null) {
            showTF.setForeground(color);
        }

        // 确定和取消按钮
        add(ok);
        ok.setBounds(227, 255, 60, 22);
        add(cancel);
        cancel.setBounds(297, 255, 60, 22);
        // 布局控件_结束

        // listener.....
        /* 用户选择字体 */
        lstFont.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                current_fontName = (String) lstFont.getSelectedValue();
                txtFont.setText(current_fontName);
                showTF.setFont(new Font(current_fontName, current_fontStyle,
                    current_fontSize));
            }
        });

        /* 用户选择字型 */
        lstStyle.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String value = (String) ((JList) e.getSource())
                    .getSelectedValue();
                if (value.equals("常规")) {
                    current_fontStyle = Font.PLAIN;
                }
                if (value.equals("斜休")) {
                    current_fontStyle = Font.ITALIC;
                }
                if (value.equals("粗休")) {
                    current_fontStyle = Font.BOLD;
                }
                if (value.equals("粗斜休")) {
                    current_fontStyle = Font.BOLD | Font.ITALIC;
                }
                txtStyle.setText(value);
                showTF.setFont(new Font(current_fontName, current_fontStyle,
                    current_fontSize));
            }
        });

        /* 用户选择字体大小 */
        lstSize.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                current_fontSize = (Integer) sizeMap.get(lstSize
                    .getSelectedValue());
                txtSize.setText(String.valueOf(current_fontSize));
                showTF.setFont(new Font(current_fontName, current_fontStyle,
                    current_fontSize));
            }
        });

        /* 用户选择字体颜色 */
        cbColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current_color = (Color) colorMap.get(cbColor.getSelectedItem());
                showTF.setForeground(current_color);
            }
        });
        /* 其它颜色 */
        otherColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new JColorChooser();
                Color col_temp = JColorChooser.showDialog(null, null, Color.pink);
                if (col_temp != null) {
                    current_color = col_temp;
                    showTF.setForeground(current_color);
                    super.mouseClicked(e);
                }
            }
        });
        /* 用户确定 */
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /* 用户用户选择的字体设置 */
                setSelectedfont(new Font(current_fontName, current_fontStyle,
                    current_fontSize));
                /* 用户用户选择的颜色设置 */
                setSelectedcolor(current_color);
                dialog.dispose();
                dialog = null;
            }
        });

        /* 用户取消 */
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                dialog = null;
            }
        });
    }

    /* 显示字体选择器对话框(x,y表示窗体的启动位置) */
    public void showDialog(Frame parent, int x, int y) {
        String title = "字体";
        dialog = new JDialog(parent, title, true);
        dialog.getContentPane().add(this);
        dialog.setResizable(false);
        dialog.setSize(400, 310);
        // 设置接界面的启动位置
        dialog.setLocation(x, y);
        dialog.addWindowListener(new WindowAdapter() {

            /* 窗体关闭时调用 */
            public void windowClosing(WindowEvent e) {
                dialog.removeAll();
                dialog.dispose();
                dialog = null;
            }
        });
        dialog.setVisible(true);
    }
}
