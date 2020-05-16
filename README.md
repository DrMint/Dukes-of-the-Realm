[//]: # (The file is written in Markdown. You can read it in its formatted form at https://github.com/DrMint/Dukes-of-the-Realm)

# Dukes of the Realm
### Description
Dukes of the Realm is a single player top-down strategy game.
The game takes place in a faraway, made-up kingdom (with regions that sound oddly French). Multiple dukes are fighting to obtain supremacy over the entire kingdom, and thus, one must conquer all others in order to triumph.

Each duke starts with a castle that produces money at set intervals. This treasury can be used to train new soldiers or level up the castle. A duke can select some soldiers and decide to attack another castle.

The game ends once all castles are overruled by a single duke, and no other moving army is still present.

### The main interface
![](https://www.r-entries.com/etuliens/img/Dukes/4.PNG)

### Troops sent by the currently selected castle
![](https://www.r-entries.com/etuliens/img/Dukes/7.PNG)

### Sending troops to another castle
![](https://www.r-entries.com/etuliens/img/Dukes/6.PNG)

## Requirements
### Compilation
In order to compile this program, you will need the `JRE System Librairy`.
The Java Build Path must include `Dukes-of-the-Realm/resources` and `Dukes-of-the-Realm/src`.

The resources are encoded in UTF-8. In order to use them currently, your IDE must recodes them in this format. In Eclipse for example, this can be done in Project -> Properties -> Resource -> Text file encoding -> Other -> UTF-8.

### Documentation
In order to generate the Javadoc, you will need to add this option:
`-classpath "[JRE installation folder]\lib\ext\jfxrt.jar"`


## Game rules

### Terms
- **Castle**: a castle is represented by a square and a rectangle. The square has the same color has its duke. The rectangle is representing the castle's entrance which can face north, east, south, or west.

- **Duke**: a duke is the owner of one or more castle. A duke can be played by a player or an <abbr title="Non playable character">NPC</abbr>. Each duke has a certain color in order to easily differentiate their castle(s) and outgoing troop(s).

- **Neutral dukes**: some dukes are neutral, which means that they will not attack any other castle. However, they will defend themselves if attacked. All neutral dukes have the color grey. Neutral castles are a good way to gain a lot of money as they usually start with plenty of it.

- **Troops**: troops are soldiers loyal to a duke. They are represented by a small square the same color as the duke that sent them. A few types of troops exists, each of them having different pros and cons. Read [Types of troops](https://github.com/DrMint/Dukes-of-the-Realm#types-of-troops) for more information.

- **Turn**: Each turn, the castle receives some gold and its production advance. The troops on also move according to their speed.

### Beginning
Each duke is given one castle to start with. Non-neutral dukes (the player and the NPCs) starts with 0 gold, 0 troop and a level 1 castle. Neutral dukes can start with a castle of higher level and many troops. They also have plenty of gold.

Neutral castles start at level 1 with one knight and two spearmen. They are instantaneously granted a random number of levels in between 1 and 10. For each of those additional levels, the castle also acquires a random troop.

### Actions during the game
#### Productions
The player can train new troops and also level up its castle. Those two actions are called productions. The list of due productions can be found in the productions list.
Productions cost money and time, and only one production can be worked on at a time. Levelling up a castle costs `1000 * its level` and takes `100 + 50 * its level` turns.

Each turn, a castle produces a certain amount of money: `10 * its level` gold. A neutral castle only produces one tenth of this amount.

#### Attacks
The player can send troops to other castles. If the target castle is also owned by the player this will transfer them to it. Otherwise, this will launch an attack. Each turn, only up to three troops can exit through the castle's door.

When sent troops arrive to an enemy castle, they attack it. The castle stored troops defend the castle. Each troop has a amount of health and damage. If a troop's health reach 0, they die. If a castle is left without any troops to defend it, it is taken by the attacking forces.

It is entirely possible that at the end of an attack, all soldiers on each side are dead. In which case, the castle is left without protection but is still owned by its original duke.

If a castle has sent troops and then get conquered, the troops will still continue to travel toward their target.

To explain how damage are managed, let's say we have 5 spearmen against a single knight. 5 spearmen have altogether 5 points of health and deal 5 damages. A knight has only 3 points of damage but 5 point of damage. At the end of the turn, all the troops on each side are dead and the castle in not conquered. This is because the attacks are done simultaneously on each side. However, this example is probably not replicable in game because, as explain earlier, only three soldiers can be sent each turn thought its door. All the soldiers will arrive at slightly different times and thus, the knight will die after killing 3 spearman and then the castle will be taken.


### Ending
The game ends when all castles are owned by a single duke and no troops from another duke are travelling.

### Types of troops

|  Troop type | Production cost | Time to produce | Speed (cell/turn)| Health | Damage |
| :------------ |:------------------:|:-------------------:|:------------------:|:-------:|:----------:|
| Spearman   | 100                     |    5                       |   2                       | 1         |  1            |
| Knight        | 500                      |    20                     |   4                      | 3         |  5            |
| Catapult     | 1000                    |    50                     |   1                       | 5        |  10          |

## Interaction
### Status bar
When the player clicks on any castle, the following information are shown on the status bar: the name of the castle, the name of its duke, its level, amount of gold, the number of each type of troops, and the total number of troops.

If the selected castle is own by the player, new troops can be created by clicking the `+` button next to each type of troop. Hovering over each of these buttons will show the troop characteristics and cost. Of course, if the amount of gold isn't enough, no new troop will be added to the production list. The castle can also be levelled up by clicking the `+` button next to the current level.

The production list is only shown when it is not empty. It lists all ongoing production which can be a new troop or the castle levelling up. Each production progress is shown after its name (for example: Knight (6/20) means that 6 turns have passed since its production begun and 20 turns are necessary for it to be complete). It is also possible to remove the current production by clicking on the `-` button next to it. When more than one production is in listed, you will also be able to click the `Remove all` button to remove all productions. When a production is removed, ` 1 - time elapsed / production time` of its cost is given back to the castle.

It is also possible to save and load the last saved game. Simply click on the `Save` or `Load` button at the right of the status bar.

Finally, the player can click on the `Send` button in order to send an attack. This will open the "Current attacks pop-up".

### Current attacks pop-up
This pop-up shows all current attacks for the selected castle. For each attack, the target castle and the number soldier (of each type) is shown.
Clicking on the `Send troops` will hide the Current attacks pop-up in order for the player to click on a target castle (which must be different from the currently selected castle). This will then open the New attack pop-up.

### New attack pop-up
From this pop-up, the player is able to select how many troops of each type to send. For each troop type, the number on the left is how many is available, and on the right is how many will be sent. Pressing the `+` will send one more troop of that type.
Once the attack is planned, clicking on the `Send troops` will launch the attack.

### Keyboard
As the game was created to be eSport ready (sarcasm alert), this game allows all of its commands to be inputted by the keyboard (except for selecting castles which must be done with the mouse).

|  Key            | Main menu         | Current attacks pop-up | New attack pop-up|
| :------------ |:------------------:|:-------------------:|:------------------:|
| `Space`       | Pause / Unpause|                           |                         |
| `Escape`     | Close the game  |  Close the game   |  Close the game  |
| `0`              | Level up the castle              |                        |                         |
| `1`              | Add spearman  to production |  |   Add spearman to the attack |
| `2`              | Add knight  to production |  |   Add knight to the attack |
| `3`              | Add catapult  to production |  |   Add catapult to the attack |
| `Enter`       | Open Current attacks pop-up                    |    Open New attack pop-up   |   Confirm new attack       |
| `Decimal`  | Cancel current production   |    Close pop-up    |   Close pop-up  |

## NPCs
NPCs are dukes controlled by the computer. They currently are extremely poor AIs, as their actions is mostly at random.

The NPCs have three possible actions:
- Adds a new troop to production (at least every 50 turns).
- Sends troops (at least every 200 turns). To select which troop to send, it basically tosses a coin, which means that on average, half of its troops are sent. The target castle is also chosen at random amongst the list of all castle.
- Levels up the castle (at least every 500 turns). It does this action only if the castle treasury is at least double the amount necessary to level up.

Those actions happen at random interval which is picked in between half the maximum number of turn and the maximum number of turns.

When a NPCs own multiple castle, the actions are applied to a random castle of its own. Also, the timers before the next actions is divided by the number of own castles, which means that the NPCs goes a lot faster after conquering a few castles.


## Settings
Unfortunately, the game doesn't currently have a Settings menu, nor launch parameters. To tweak the options, you will need to open Settings.java and modify it. A lot of options can be found in there, the most noticeable being:

- `WINDOW_WIDTH` and `WINDOW_HEIGHT` to change the window resolution.
- `GRID_WIDTH` and `GRID_HEIGHT` to change the grid size.
- `TURN_DURATION` to change the duration of a turn.
- `LANGUAGE` to change the game language.
- `NUM_NPC` the number of NPCs

Many more can be found in this file. You can read the complete list in the Javadoc.

## Future plans

### Currently known bugs or limitation
- It is not currently possible to resize the game window after launch.
- Castles are placed at random with a certain distance of each other. Because of the way this is implemented, this process is calculated in `O(x * y)` for a grid of size `x * y`.
- There is a bug when the turn duration is set too low. The thing is, when the computer isn't able to keep up with the game, some outgoing troops stop moving for a while, then start moving again when less troops are shown on the screen. This only happens when they are too many troops moving and the turn duration is somewhere near 40ms (normally it is in between 150 and 300).
