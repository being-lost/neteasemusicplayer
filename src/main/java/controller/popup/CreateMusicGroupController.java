package controller.popup;

import application.SpringFXMLLoader;
import controller.component.MusicGroupTabController;
import controller.main.LeftController;
import controller.main.MainController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import util.WindowUtils;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author super lollipop
 * @date 19-11-30
 */
@Controller
public class CreateMusicGroupController {

    /**错误提示信息*/
    private String ERROR_MESSAGE = "歌单名不能为空";

    /**"输入歌单名词"的TextField组件*/
    @FXML
    private TextField tfInput;

    /**“新建”按钮组件*/
    @FXML
    private Button btnCreate;

    /**"取消"按钮组件*/
    @FXML
    private Button btnCancel;

    /**输入错误的提示label*/
    @FXML
    private Label labInputError;

    /**注入窗体根容器（BorderPane）的控制类*/
    @Resource
    MainController mainController;


    /**注入Spring上下文工具类*/
    @Resource
    private ConfigurableApplicationContext applicationContext;

    /**注入左边放置标签的容器控制器*/
    @Resource
    LeftController leftController;

    /**初始化函数，自动调用，以前需要实现接口Initializable,从JavaFX2.0开始就自动会调用了，不需要实现接口*/
    public void initialize(){
        labInputError.setText("");  //初始化不提示信息
        //为提供输入歌单名称的TextField添加文本属性监听器，当文本内容为空时，提示信息“歌单名不能为空”
        tfInput.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.trim().equals("")) {
                    labInputError.setText(ERROR_MESSAGE);
                }
                else {
                    labInputError.setText("");
                }
            }
        });

    }

    /**“新建”按钮鼠标单击事件处理*/
    @FXML
    public void onCreateButtonClicked(MouseEvent mouseEvent) throws IOException{
        if (mouseEvent.getButton() == MouseButton.PRIMARY){  //鼠标左击
            if (!labInputError.getText().equals(ERROR_MESSAGE)){  //如果显示错误信息的label文本内容不是错误信息内容
                if (tfInput.getText().trim().equals("")
                        &&tfInput.getText().trim().length()==0){     //如果输入内容的TextField组件没有合法内容，直接调用取消按钮的事件处理，隐藏窗体
                    this.onCancelButtonClicked(mouseEvent);
                }
                else {  //否则
                    this.createMusicGroup(tfInput.getText());   //调用创建音乐歌单的函数
                }
            }
        }
    }

    /**创建音乐歌单的函数*/
    private void createMusicGroup(String groupName) throws IOException {
        System.out.println("create");
        //UI更新部分
        FXMLLoader fxmlLoader = applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/component/musicgroup-tab.fxml");
        leftController.getVBoxTabContainer().getChildren().add(fxmlLoader.load());
        MusicGroupTabController musicGroupTabController = fxmlLoader.getController();
        musicGroupTabController.getLabGroupName().setText(groupName);   //设置Label显示内容为输入的歌单名称
        leftController.getTabList().add(musicGroupTabController.getHBoxMusicGroup());

        ((Stage)tfInput.getScene().getWindow()).close();
        WindowUtils.releaseBorderPane(mainController.getBorderPane());    //释放borderPane的鼠标事件并且还原透明度
    }

    /**"取消"按钮鼠标单机事件处理*/
    @FXML
    public void onCancelButtonClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            (tfInput.getScene().getWindow()).hide();  //隐藏窗体
            WindowUtils.releaseBorderPane(mainController.getBorderPane());    //释放borderPane的鼠标事件并且还原透明度
        }
    }

}