package oogasalad.engine.newconfig;

import java.util.List;
import oogasalad.engine.newconfig.model.Level;
import oogasalad.engine.newconfig.model.Metadata;
import oogasalad.engine.newconfig.model.Settings;

public record GameConfig(Metadata metadata, Settings settings, List<Level> levels,
                         String gameFolderPath) {

}
