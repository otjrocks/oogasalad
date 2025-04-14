//package oogasalad.player.model.control.targetcalculation;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import oogasalad.engine.model.EntityPlacement;
//import oogasalad.engine.model.EntityType;
//import oogasalad.engine.model.GameMap;
//import oogasalad.player.model.exceptions.TargetStrategyException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class TargetStrategyFactoryTest {
//
//  private EntityPlacement mockPlacement;
//  private EntityType mockEntityType;
//  private GameMap mockMap;
//
//  @BeforeEach
//  void setUp() {
//    mockPlacement = mock(EntityPlacement.class);
//    mockEntityType = mock(EntityType.class);
//    mockMap = mock(GameMap.class);
//
//    when(mockPlacement.getType()).thenReturn(mockEntityType);
//  }
//
//  @Test
//  void createTargetStrategy_validEntityStrategy_returnsInstance() {
//    Map<String, Object> config = new HashMap<>();
//    config.put("targetType", "enemy");
//
//    when(mockEntityType.controlType()).thenReturn("targetEntity");
//    when(mockEntityType.strategyConfig()).thenReturn(config);
//
//    TargetStrategy strategy = TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap);
//
//    assertNotNull(strategy);
//    assertInstanceOf(TargetEntityStrategy.class, strategy);
//  }
//
//  @Test
//  void createTargetStrategy_validAheadStrategy_returnsInstance() {
//    Map<String, Object> config = new HashMap<>();
//    config.put("targetType", "enemy");
//    config.put("tilesAhead", 1);
//
//    when(mockEntityType.controlType()).thenReturn("targetAheadOfEntity");
//    when(mockEntityType.strategyConfig()).thenReturn(config);
//
//    TargetStrategy strategy = TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap);
//
//    assertNotNull(strategy);
//    assertInstanceOf(TargetAheadOfEntityStrategy.class, strategy);
//  }
//
//  @Test
//  void createTargetStrategy_invalidControlType_throwsException() {
//    when(mockEntityType.controlType()).thenReturn("nonExistentStrategy");
//    when(mockEntityType.strategyConfig()).thenReturn(new HashMap<>());
//
//    assertThrows(TargetStrategyException.class,
//        () -> TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap));
//  }
//
//  @Test
//  void createTargetStrategy_malformedControlType_stillCapitalizesCorrectly() {
//    Map<String, Object> config = new HashMap<>();
//    config.put("targetType", "enemy");
//
//    when(mockEntityType.controlType()).thenReturn("target_entity");
//    when(mockEntityType.strategyConfig()).thenReturn(config);
//
//    TargetStrategy strategy = TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap);
//    assertNotNull(strategy);
//    assertInstanceOf(TargetEntityStrategy.class, strategy);
//  }
//
//  @Test
//  void createTargetStrategy_missingConstructor_throwsException() {
//    // simulate a controlType that refers to a class that doesn't match expected constructor
//    when(mockEntityType.controlType()).thenReturn("string"); // java.lang.String doesn't have right constructor
//    when(mockEntityType.strategyConfig()).thenReturn(new HashMap<>());
//
//    assertThrows(TargetStrategyException.class,
//        () -> TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap));
//  }
//}
