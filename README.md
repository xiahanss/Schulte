# Schulte/舒尔特方块

Add the dependency/引用:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.lannaican:schulte:Tag'
}
```

Create a game/创建游戏
```
SchulteGame game = new SchulteGame();
game.setRow(5);
game.setColumn(5);
game.setConfig(SchulteConfig);
game.setListener(SchulteListener);
schulteView.setGame(game);
```

Update view/更新游戏界面
```
schulteView.update();
```

Start Game/开始游戏
```
game.start();
```

SchulteConfig/配置

| Prop Name | Default Value |
| ----  | ---- |
| borderColor | #CCCCCC |
| borderSize | 0.2F |
| cellColor | #FFFFFF |
| cellPressColor | #F0F0F0 |
| fontColor | #666666 |
| fontSize | 0.6F |
| corner | 0.1F |
| countDownTime | 3000 |
| animation | true |
