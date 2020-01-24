package service;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.LocalSong;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import task.LoadSongTask;

import javax.annotation.Resource;

/**
 * @author super lollipop
 * @date 19-12-2
 */
@Component
@Scope("prototype")
public class LoadSongService extends Service<ObservableList<LocalSong>> {

    @Resource
    private LoadSongTask loadSongTask;

    @Override
    protected Task<ObservableList<LocalSong>> createTask() {
        return loadSongTask;
    }
}
