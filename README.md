# Schulte/舒尔特方块

[![](https://jitpack.io/v/lannaican/schulte.svg)](https://jitpack.io/#lannaican/schulte)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

#### Add the dependency/引用:

```java
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.lannaican:schulte:1.0.17'
}
```

#### Create a game/创建游戏
```java
SchulteGame game = new SchulteGame();
game.setRow(5);
game.setColumn(5);
game.setConfig(SchulteConfig);
game.setListener(SchulteListener);
schulteView.setGame(game);
```

#### Update view/更新游戏界面
```java
schulteView.update();
```

#### Start Game/开始游戏
```java
game.start();
```

#### SchulteConfig/配置

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

Don't forget update() after config changed.

#### Other Method
```java
game.getStatus();
game.getTapTotal();
game.getTapCorrect();
game.getTapError();
SchulteUtil.createCell(row, column);
```

#### APP
[扫雷大作战](costars.cn)
