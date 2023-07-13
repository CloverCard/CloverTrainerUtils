<!-----

Yay, no errors, warnings, or alerts!

Conversion time: 0.868 seconds.


Using this HTML file:

1. Paste this output into your source file.
2. See the notes and action items below regarding this conversion run.
3. Check the rendered output (headings, lists, code blocks, tables) for proper
   formatting and use a linkchecker before you publish this page.

Conversion notes:

* Docs to Markdown version 1.0β34
* Thu Jul 13 2023 09:04:41 GMT-0700 (PDT)
* Source doc: CloverTrainerUtils_Documentation
* Tables are currently converted to HTML tables.
----->


<h2>CloverTrainerUtils Documentation</h2>


<h3>Introduction</h3>


<p>
In Pixelmon Reforged, there are many options for customizing trainers such as battle rules, dialogue, drops, and teams; however, these features by themselves can be very limiting for server owners who may want far more customizability. One common option many take is using the Trainer Commands sidemod to run commands to perform actions during common battle or interaction events; however this only allows them to perform actions that can be done through commands, and nothing more. This mod seeks to expand on that and provide far more customizability using a Pure Forge approach for wider usage.
</p>
<hr>
<h3>Features</h3>


<p>
<strong>Instanced Battles (Mandatory): </strong>Starting a battle with a trainer starts a battle with an invisible cloned copy of the trainer which deletes itself at the end of the battle. This allows for multiple people to be battling the same trainer at the same time.
</p>
<p>
<strong>Conditional Drops: </strong>Allows a server/staff member to add an item OR command with a drop rate (1-100%) to a trainer. This can be done individually in game or defined as a preset group within the configuration files.
</p>
<p>
<strong>Team Shuffling: </strong>Allows for a trainer to shuffle teams defined in the mod’s configuration files.
</p>
<p>
<strong>Checkpoints: </strong>Prevents someone from fighting a trainer unless they have the defined access tag AND/OR rewards someone an access tag upon beating a trainer. This allows for potential gatekeeping or progression in servers.
</p>
<p>
<strong>Commands: </strong>Replicates trainer commands with a more user friendly command structure. (Note: This mod and trainer commands do not mix well together when on the same trainer, hence why this feature is necessary).
</p>
<hr>
<h3>Getting Started</h3>


<p>
*Note: To use this mod, you need both Pixelmon Reforged (Version: 1.16.5-9.1.5) and at least Minecraft Forge (Version: 36.2.34).
</p>
<ol>

<li>After downloading the mod, place it within your mods folder. (Note: mods folder NOT plugins folder). 

<li>Start your server. If everything works fine, your server should start up with no problems, and the default configuration files should generate in the /config/CloverTrainerUtils folder.

<li>Log in to your server and pick an NPC trainer of your choice and run the command, “/tutils init”.

<li>Congratulations! You’ve set up your first trainer for this mod. To see further information on what else you can do, you can use “/tutils about” to see all commands, or further check this documentation.
</li>
</ol>
<hr>
<h3>Commands</h3>


<h4>Basic</h4>



<table>
  <tr>
   <td><strong>Function</strong>
   </td>
   <td><strong>Command</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>Initializing Data for the Mod
   </td>
   <td>/tutils init
   </td>
   <td>Set up a trainer for this mod. Without any other commands, it allows all of their fights to be instanced.
   </td>
  </tr>
  <tr>
   <td>Clearing Mod Data
   </td>
   <td>/tutils clear
   </td>
   <td>Removes all data associated with CloverTrainerUtils from a trainer, making it a normal trainer once again.
   </td>
  </tr>
  <tr>
   <td>Display Mod Information
   </td>
   <td>/tuils about
   </td>
   <td>Displays commands and their descriptions within the game chat.
   </td>
  </tr>
</table>


<h4>Checkpoints</h4>



<table>
  <tr>
   <td><strong>Function</strong>
   </td>
   <td><strong>Command</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>Add Checkpoint
   </td>
   <td>/tutils checkpoints add [access_tag] [rewarded_tag]:
   </td>
   <td>Sets the tag required to access the trainer and the tag rewarded for beating the trainer.
   </td>
  </tr>
  <tr>
   <td>Add Checkpoint (Only reward tag)
   </td>
   <td>/tutils checkpoints begin [rewarded_tag]
   </td>
   <td>Sets the tag rewarded for beating the trainer and removes any tag required to access the trainer.
   </td>
  </tr>
  <tr>
   <td>Add Checkpoint (Only access tag)
   </td>
   <td>/tutils checkpoints finish [access_tag]
   </td>
   <td>Sets the tag required to access a trainer and removes any tag rewarded for beating the trainer.
   </td>
  </tr>
  <tr>
   <td>View Checkpoints
   </td>
   <td>/tutils checkpoints list
   </td>
   <td>Displays the current tags.
   </td>
  </tr>
  <tr>
   <td>Clear Checkpoints
   </td>
   <td>/tutils checkpoints clear
   </td>
   <td>Removes all tags on a trainer.
   </td>
  </tr>
</table>


<h4>Conditional Drops</h4>



<table>
  <tr>
   <td><strong>Function</strong>
   </td>
   <td><strong>Command</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>Add Conditional Drop
   </td>
   <td>/tutils conddrops add [item OR cmd] [probability (1-100)] [quantity (1 or Greater)] [item id (i.e pixelmon:master_ball) OR command (i.e. say hello)]
   </td>
   <td>Adds an item/cmd drop with a conditional drop rate to the trainer.
   </td>
  </tr>
  <tr>
   <td>Add Conditional Drops using a Preset
   </td>
   <td>/tutils conddrops addpreset [preset_name]
   </td>
   <td>Adds a list of conditional drops defined within the configuration files.
   </td>
  </tr>
  <tr>
   <td>View Conditional Drops
   </td>
   <td>/tutils conddrops list
   </td>
   <td>Lists a trainer's conditional drops in the order added.
   </td>
  </tr>
  <tr>
   <td>Clear Conditional Drops
   </td>
   <td>/tutils conddrops clear
   </td>
   <td>Removes all of a trainer's conditional drops.
   </td>
  </tr>
  <tr>
   <td>Remove a Single Conditional Drop
   </td>
   <td>/tutils conddrops remove [order_number]
   </td>
   <td>Removes a conditional drop currently in the order number's position.
   </td>
  </tr>
</table>


<h4>Trainer Commands</h4>



<table>
  <tr>
   <td><strong>Function</strong>
   </td>
   <td><strong>Command</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>Add Trainer Command
   </td>
   <td>/tutils commands add [start OR forfeit OR playerwins OR playerloses] [command]
   </td>
   <td>Performs a command after the specified event happens.
   </td>
  </tr>
  <tr>
   <td>View Trainer Commands
   </td>
   <td>/tutils commands list [start OR forfeit OR playerwins OR playerloses]
   </td>
   <td>Lists the commands that are performed after the specified event in order added.
   </td>
  </tr>
  <tr>
   <td>Remove A Trainer Command
   </td>
   <td>/tutils commands remove [start OR forfeit OR playerwins OR playerloses] [order_number]
   </td>
   <td>Removes the command at the position of the order number.
   </td>
  </tr>
  <tr>
   <td>Clear Trainer Commands
   </td>
   <td>/tutils commands clear [start OR forfeit OR playerwins OR playerloses]
   </td>
   <td>Clears all commands on a trainer for the specified event.
   </td>
  </tr>
</table>


<h5>Special Command Placeholders</h5>



<table>
  <tr>
   <td>Player Placeholder
   </td>
   <td>@PL
   </td>
   <td>Gets the player who this command is based around.
   </td>
  </tr>
  <tr>
   <td>Player Runs Command Placeeholder
   </td>
   <td>@PCMD
   </td>
   <td>Makes the player run the command rather than the server.
   </td>
  </tr>
  <tr>
   <td>Delay Placeholder
   </td>
   <td>@D:#
   </td>
   <td>Adds # seconds delay until a command is run.
   </td>
  </tr>
</table>


<h4>Team Shuffler</h4>



<table>
  <tr>
   <td><strong>Function</strong>
   </td>
   <td><strong>Command</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>Add a Team
   </td>
   <td>/tutils shuffler add [team_id]
   </td>
   <td>Adds a team defined in the config to potentially select at the start of a battle.
   </td>
  </tr>
  <tr>
   <td>Remove a Team
   </td>
   <td>/tutils shuffler remove [team_id]
   </td>
   <td>Removes the team id provided from the ids a trainer can shuffle through.
   </td>
  </tr>
  <tr>
   <td>Clear Teams
   </td>
   <td>/tutils shuffler clear
   </td>
   <td>Clears all the teams a trainer can shuffle through.
   </td>
  </tr>
  <tr>
   <td>View Teams
   </td>
   <td>/tutils shuffler list
   </td>
   <td>Lists all the team ids the trainer can shuffle through.
   </td>
  </tr>
</table>


<hr>
<h3>Configuration</h3>


<h4>Conditional Drop Presets</h4>


<p>
The conditional drops configuration file can be found in the /config/CloverTrainerUtils/conddrops folder. Within the json file, multiple different presets can be defined under the map called, “presets”. The name given is what is needed to use when using the preset command. In Example 1, for example, this is “example”
</p>
<p>
Each preset functions as a list containing multiple drop objects. These drop objects consist of four fields which are type, data, prob, and quantity.
</p>

<table>
  <tr>
   <td>type
   </td>
   <td>Can either be item or cmd, which indicate whether the drop is an item or a command.
   </td>
  </tr>
  <tr>
   <td>data
   </td>
   <td>When type is item, data is the item’s id such as “pixelmon:master_ball”
<p>
When type is cmd, data is the command itself such as “say Hello World!”
   </td>
  </tr>
  <tr>
   <td>prob
   </td>
   <td>The probability in percentage of the drop happening. It can only be from 1-100
   </td>
  </tr>
  <tr>
   <td>quantity
   </td>
   <td>The amount of the item given or the amount of times the command is run.
   </td>
  </tr>
</table>


<h5>Example 1</h5>





<pre class="prettyprint">{
  "presets": {
    "example": [
      {
        "type": "item",
        "data": "pixelmon:great_ball",
        "prob": 100,
        "quantity": 5
      },
      {
        "type": "cmd",
        "data": "say Hello World!",
        "prob": 100,
        "quantity": 1
      }
    ]
  }
}
</pre>


<h4>Team Shuffler</h4>


<p>
The team shuffler configuration files can be found in the /config/CloverTrainerUtils/shuffler folder. The files consist of two config files. The pokemon.json file is used to define the pokemon that can be used in a team, and the teams.json is used to connect the ids from the pokemon.json with a team id to be used with shuffler commands.
</p>
<h5>Pokemon Configuration</h5>


<p>
Within the pokemon.json, all pokemon objects are defined within the “pokemon” map. In Example 2 examples of these pokemon objects are labeled “example1”  and  “example2”. Each has a variety of fields to create the pokemon.
</p>

<table>
  <tr>
   <td><strong>Field</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>species
   </td>
   <td>The species name of the pokemon.
   </td>
  </tr>
  <tr>
   <td>form
   </td>
   <td>The form name of the pokemon
   </td>
  </tr>
  <tr>
   <td>gender
   </td>
   <td>The gender name of the pokemon
   </td>
  </tr>
  <tr>
   <td>ability
   </td>
   <td>The ability name of the pokemon
   </td>
  </tr>
  <tr>
   <td>nature
   </td>
   <td>The nature name of the pokemon
   </td>
  </tr>
  <tr>
   <td>heldItem
   </td>
   <td>The held item id of the item.
   </td>
  </tr>
  <tr>
   <td>ivs
   </td>
   <td>A list of the pokemon’s ivs from 0-31 in the order of Hp, Attack, Defense, Special Attack, Special Defense, and Speed.
   </td>
  </tr>
  <tr>
   <td>evs
   </td>
   <td>A list of the pokemon’s ivs from 0-252 in the order of Hp, Attack, Defense, Special Attack, Special Defense, and Speed.
   </td>
  </tr>
  <tr>
   <td>moveset
   </td>
   <td>A list of four move names that will make up the pokemon’s moveset.
   </td>
  </tr>
</table>


<h6>Example 2</h6>





<pre class="prettyprint">{
  "pokemon": {
    "example2": {
      "species": "Bidoof",
      "form": "",
      "gender": "female",
      "ability": "Moody",
      "nature": "Adamant",
      "heldItem": "pixelmon:leftovers",
      "ivs": [
        31,
        31,
        31,
        31,
        31,
        31
      ],
      "evs": [
        252,
        0,
        0,
        0,
        0,
        252
      ],
      "moveset": [
        "Tackle",
        "Growl",
        "Roar",
        "Sheer Cold"
      ]
    },
    "example1": {
      "species": "Pikachu",
      "form": "",
      "gender": "female",
      "ability": "Moody",
      "nature": "Adamant",
      "heldItem": "pixelmon:leftovers",
      "ivs": [
        31,
        31,
        31,
        31,
        31,
        31
      ],
      "evs": [
        252,
        0,
        0,
        0,
        0,
        252
      ],
      "moveset": [
        "Tackle",
        "Growl",
        "Roar",
        "Sheer Cold"
      ]
    }
  }
}
</pre>


<h5>Teams Configuration</h5>


<p>
Within the json file, multiple different teams can be defined under the map called, “teams”. The name given is what is needed to use when using the shuffler command. In Example 3, for example, this is “exampleteam”. Within “exampleteam”, up to six slots can be with the contents of each slot being the id of the pokemon object within the pokemon.json file.
</p>
<h6>Example 3</h6>





<pre class="prettyprint">{
  "teams": {
    "exampleteam": {
      "slot1": "example1",
      "slot2": "example2"
    }
  }
}
