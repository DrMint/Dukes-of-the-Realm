[//]: # (The file is written in Markdown)

# Dukes of the Realm
### Description
Dukes of the Realm is a single player top-down strategy game.
The game takes place in a faraway, made-up kingdom (with regions that sound oddly French). Multiple dukes are fighting to obtain supremacy over the entire kingdom, and thus, one must conquer all others in order to triumph.

Each duke starts with a castle that produces money at set intervals. This treasury can be used to train new soldiers or level up the castle. A duke can select some soldiers and decide to attack another castle.

The game ends once all castles are overruled by a single duke, and no other moving army are still present.

## Requirements
### Compilation
In order to compile this program, you will need the `JRE System Librairy`.
The Java Build Path must include `Dukes-of-the-Realm/resources` and `Dukes-of-the-Realm/src`.

### Documentation
In order to generate the Javadoc, you will need to add this option:
`-classpath "[JRE installation folder]\lib\ext\jfxrt.jar"`


## Game rules

### Terms
- **Castle**: a castle is represented by a square and a rectangle. The square has the same color has its duke. The rectangle is representing the castle's entrance which can face north, east, south, or west.

- **Duke**: a duke is the owner of one or more castle. A duke can be played by a player or an <abbr title="Non playable character">NPC</abbr>. Each duke has a certain color in order to easilly differenciate their castle(s) and outgoing troop(s).

- **Neutral dukes**: Some dukes are neutral, which means that they will not attack any other castle. However, they will defend themselves if attacked. Neutral castles are a good way to gain a lot of money as they usually starts with plenty of it. Furthermore, neutral dukes have a random level; the higher their level is, the harder it will be to attack them (they have one more random troop for each additional level).

- **Troops**: Troops are soldiers loyal to a duke. A few types of troops exists, each of them having different pros and cons. Read [Types of troops](https://github.com/DrMint/Dukes-of-the-Realm "Types of troops") for more information.

### Beginning
Each dukes are given one castle to start with. Non-neutral dukes (the player and the NPCs) starts with 0 gold, 0 troops and a level 1 castle. Neutral dukes can start with a castle of higher level and many troops. They also have plenty of gold.



### Types of troops

|  Troop type | Production cost | Time to produce | Speed (cell/turn)| Health | Damage |
| :------------ |:------------------:|:-------------------:|:------------------:|:-------:|:----------:|
| Spearman   | 100                     |    5                       |   2                       | 1         |  1            |
| Knight        | 500                      |    20                     |   4                      | 3         |  5            |
| Catapult     | 1000                    |    50                     |   1                       | 5        |  10          |

### Load and save

## Options

## Future plans


### Currently known bugs or limitation
