package cn.edu.util;

import javax.swing.ImageIcon;

/**
 * 用户头像
 *
 * @author nmyphp
 * <p>MALE_ONLINE:男，在线；</p>
 * <p>MALE_NOT_ONLINE:男，不在线；</p>
 * <p>FEMALE_ONLINE:女，在线；</p>
 * <p>FEMALE_NOT_ONLINE:女，不在线；</p>
 * <p>GROUP:群组</p>
 */
public class MyImageIcon {

    public static final ImageIcon MALE_ONLINE = new ImageIcon(MyImageIcon.class.getResource("/image/faces/male.png"));
    public static final ImageIcon MALE_NOT_ONLINE = new ImageIcon(
        MyImageIcon.class.getResource("/image/faces/male_black.png"));
    public static final ImageIcon FEMALE_ONLINE = new ImageIcon(
        MyImageIcon.class.getResource("/image/faces/female.png"));
    public static final ImageIcon FEMALE_NOT_ONLINE = new ImageIcon(
        MyImageIcon.class.getResource("/image/faces/female_black.png"));
    public static final ImageIcon GROUP = new ImageIcon(MyImageIcon.class.getResource("/image/faces/group.png"));
    public static final ImageIcon LOGO_USER = new ImageIcon(MyImageIcon.class.getResource("/image/logoUser.gif"));
    public static final ImageIcon LOGO = new ImageIcon(MyImageIcon.class.getResource("/image/logo.gif"));
    public static final ImageIcon FLASHALERT = new ImageIcon(MyImageIcon.class.getResource("/image/flashAlert.gif"));
    public static final ImageIcon FONT = new ImageIcon(MyImageIcon.class.getResource("/image/toolbar/ToolbarFont.png"));
    public static final ImageIcon FACE = new ImageIcon(MyImageIcon.class.getResource("/image/toolbar/ToolbarFace.png"));
    public static final ImageIcon PICTURE = new ImageIcon(
        MyImageIcon.class.getResource("/image/toolbar/ToolbarPicture.png"));
}
