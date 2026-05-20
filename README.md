# Adventure Quest

A Java Swing RPG game built as the final project for the **Object-Oriented Programming** course (Spring 2026).

## Game Overview

Adventure Quest is a GUI-based role-playing game where players create a character, battle enemies, complete quests, collect gold, and purchase items from the shop — all through an interactive Swing interface.

### Features

- **Character Creation**: Choose from Warrior, Mage, or Archer — each with unique stats and skills
- **Turn-Based Combat**: Battle enemies with attack, defend, and class-specific abilities
- **Quest System**: Accept and complete quests for XP and gold rewards
- **Shop & Inventory**: Buy items, manage equipment, handle gold economy
- **Save/Load**: Persistent game state via file I/O
- **Leveling System**: Gain XP, level up, and upgrade stats

## Project Structure

```
adventurequest/
├── src/
│   └── adventurequest/
│       ├── Main.java                      # Application entry point
│       ├── GameFrame.java                 # Main GUI window and game loop
│       ├── GameData.java                  # Central game state management
│       ├── GameFileHandler.java           # Save/load game state
│       ├── GameState.java                 # Game state enum
│       │
│       ├── Combatable.java               # Combat behavior interface
│       ├── Character.java                # Abstract base class for all characters
│       ├── Player.java                   # Player character with leveling
│       │   ├── Warrior.java              # High HP, melee specialist
│       │   ├── Mage.java                 # High attack, magic specialist
│       │   └── Archer.java              # Balanced, ranged specialist
│       ├── Enemy.java                    # Enemy NPCs with combat AI
│       │
│       ├── BattleManager.java            # Turn-based combat logic
│       ├── BattleResult.java             # Battle outcome data
│       │
│       ├── Quest.java                    # Quest definition
│       ├── QuestLog.java                 # Active quest tracking
│       ├── QuestDetailDialog.java        # Quest detail UI
│       │
│       ├── Item.java                     # Inventory item
│       ├── ItemType.java                 # Item type enum
│       ├── Inventory.java                # Player inventory management
│       ├── InventoryPanel.java           # Inventory UI
│       ├── Shop.java                     # Shop with buy/sell logic
│       │
│       ├── BuyItemDialog.java            # Purchase UI
│       ├── CreateCharDialog.java         # Character creation UI
│       │
│       ├── InsufficientGoldException.java # Custom exception
│       └── InvalidItemException.java     # Custom exception
│
├── docs/
│   ├── OOPProjectRequirements.pdf        # Course project specification
│   ├── OOP-Project-issue.pdf            # Project issue description
│   └── proposal.tex                      # Project proposal (LaTeX)
│
├── .gitignore
└── README.md
```

## OOP Concepts Demonstrated

| Concept | Implementation |
|---------|---------------|
| **Abstract Classes** | `Character` — base for Player and Enemy |
| **Interfaces** | `Combatable` — defines combat contract |
| **Inheritance** | `Warrior`, `Mage`, `Archer` extend `Player` extends `Character` |
| **Polymorphism** | Different skill implementations per character class |
| **Encapsulation** | Private/protected fields with getter/setter methods |
| **Exception Handling** | Custom exceptions: `InsufficientGoldException`, `InvalidItemException` |
| **File I/O** | `GameFileHandler` for persistent save/load |
| **GUI (Swing)** | `GameFrame`, multiple dialog classes, panel-based layout |
| **Enums** | `ItemType`, `GameState` |

## How to Build & Run

### Prerequisites
- JDK 8 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code) or command line

### Compile
```bash
javac -d out src/adventurequest/*.java
```

### Run
```bash
java -cp out adventurequest.Main
```

## Author

Mohsi Chi — BNBU Computer Science and Technology, Class of 2029
