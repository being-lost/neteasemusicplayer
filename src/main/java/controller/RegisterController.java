package controller;

import application.SpringFXMLLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.User;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import service.UserDao;
import util.WindowUtils;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author super lollipop
 * @date 19-12-5
 */
@Controller
public class RegisterController {

    /**
     * "返回"Label图标
     */
    @FXML
    private Label labBackIcon;

    /**
     * "关闭"Label图标
     */
    @FXML
    private Label labCloseIcon;

    /**
     * "账号"的TextField组件
     */
    @FXML
    private TextField tfAccountID;

    /**
     * "清除"账号的图标
     */
    @FXML
    private Label labClearIcon;

    /**
     * "密码"的TextField组件
     */
    @FXML
    private PasswordField pfPassword;

    @FXML
    /**注册信息反馈的Label组件*/
    private Label labRegisterInformation;

    /**
     * "注册"按钮组件
     */
    @FXML
    private Button btnRegister;

    /**
     * 注入窗体根容器（BorderPane）的控制类
     */
    @Resource
    MainController mainController;

    /**注入“导航去登录、注册”面板的控制器Controller*/
    @Resource
    private NavigateLoginOrRegisterController navigateLoginOrRegisterController;

    /**注入userDao类*/
    @Resource
    private UserDao userDao;

    /**注入Spring上下文工具类*/
    @Resource
    private ConfigurableApplicationContext applicationContext;

    public void initialize() {
        labClearIcon.setVisible(false);  //初始化为不可见
        btnRegister.setMouseTransparent(true); //初始化不可以点击
        btnRegister.setOpacity(0.8);           //初始化不透明度为0.8

        Platform.runLater(() -> {
            btnRegister.requestFocus();         //"登录"按钮请求聚焦
        });

        //"清除"账号的图标的显示时机
        tfAccountID.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!tfAccountID.getText().equals("")) {
                labClearIcon.setVisible(true);
                if (!pfPassword.getText().equals("")) {
                    btnRegister.setMouseTransparent(false);
                    btnRegister.setOpacity(1);
                }

            } else {
                labClearIcon.setVisible(false);
            }
        });
        //"清除"账号的图标的显示时机
        tfAccountID.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == true && !tfAccountID.getText().equals("")) {
                labClearIcon.setVisible(true);
            } else {
                labClearIcon.setVisible(false);
            }
        }));

        //设置"注册"按钮的可点击性
        pfPassword.textProperty().addListener(((observable1, oldValue1, newValue1) -> {
            if (!pfPassword.getText().equals("") && !tfAccountID.getText().equals("")) {
                btnRegister.setMouseTransparent(false);
                btnRegister.setOpacity(1);
            } else {
                btnRegister.setMouseTransparent(true);
                btnRegister.setOpacity(0.8);
            }
        }));

    }

    /**
     * "返回"Label图标鼠标点击事件处理
     */
    @FXML
    public void onClickedBackIcon(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {  //鼠标左击
            labBackIcon.getScene().setRoot(navigateLoginOrRegisterController.getNavigateLoginOrRegister());  //设置根容器为"登录、注册的导航容器"
        }
    }

    /**
     * "关闭"Label图标鼠标点击事件处理
     */
    @FXML
    public void onClickedCloseIcon(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {  //鼠标左击
            labCloseIcon.getScene().getWindow().hide();      //关闭窗口
            WindowUtils.releaseBorderPane(mainController.getBorderPane());  //释放中间的面板，可以接受鼠标事件和改变透明度
        }

    }

    /**
     * "清除"账号的图标点击事件处理
     */
    @FXML
    public void onClickedClearIcon(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            tfAccountID.setText("");
            btnRegister.setMouseTransparent(true);
            btnRegister.setOpacity(0.8);
        }
    }

    /**
     * "注册"按钮点击事件处理
     */
    @FXML
    public void onClickedRegisterButton(ActionEvent actionEvent) throws IOException {
        if (!btnRegister.getText().equals("转到登录页面")){   //如果还没有注册成功
            User user1 = new User();         //创建用户对象，设置属性为输入的TextField文本内容
            user1.setId(tfAccountID.getText());
            user1.setPassword(pfPassword.getText());
            int row = userDao.addUser(user1);
            if (row==1){
                labRegisterInformation.setText("注册成功");
                btnRegister.setText("转到登录页面");
            }
        }
        else {  //否则,则注册成功了,按钮可以转到登录页面。
            Scene registerScene = btnRegister.getScene();
            FXMLLoader fxmlLoader = applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/login.fxml");
            registerScene.setRoot(fxmlLoader.load());
        }
    }
}