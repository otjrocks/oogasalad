package oogasalad.engine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TilesTest {

  @Test
  void tiles_copiesLayout_cannotDirectlyModifyLayoutCopiedCorrectly() {
    String[] layout = {"###", "#.#", "###"};
    Tiles tiles = new Tiles(layout);

    assertArrayEquals(layout, tiles.getLayout());

    layout[0] = "...";
    assertNotEquals(layout[0], tiles.getLayout()[0]);
  }

  @Test
  void getLayout_returnsLayout_layoutIsCorrectAndD() {
    String[] layout = {"###", "#.#", "###"};
    Tiles tiles = new Tiles(layout);

    String[] layoutCopy = tiles.getLayout();
    layoutCopy[0] = "...";

    assertEquals("###", tiles.getLayout()[0]);
    assertEquals("#.#", tiles.getLayout()[1]);
    assertEquals("###", tiles.getLayout()[2]);
  }

  @Test
  void setLayout_copiesSetLayout_cannotDirectlyModifyLayoutCopiedSafely() {
    Tiles tiles = new Tiles(new String[]{"abc"});
    assertEquals("abc", tiles.getLayout()[0]);

    String[] layout = {"###", "#.#", "###"};
    tiles.setLayout(layout);

    layout[0] = "...";
    assertEquals("###", tiles.getLayout()[0]);
    assertEquals("#.#", tiles.getLayout()[1]);
    assertEquals("###", tiles.getLayout()[2]);
  }

}
