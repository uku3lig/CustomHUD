## WIP CustomHud v4 Changelog

### New Config Screen
- Unlimited Profiles
- Ability to name profiles
- Ability to bind [toggles](link_here)
- Ability to set a keybind to switch to that profile
- Ability to set a keybind to switch to that profile
- Ability to reorder profiles
- Decide which profiles are used when using the cycle profiles keybind
- Buttons to go to the docs and discord
![SCREENSHOT HERE]()

### Language Features
- Added List Variable Support
  -  `{<list>}` alone will just show how many there are
  - Example: `{effects}` → 3 (assuming the player has 3 status effects)
  - Inline: `{<list>, "stuff here"}`
    - Example: `{effects, "{index} - {icon} {name}\n"}`
  - Multiline: `=for:<list>=` ends with `=endfor=`
  - Each list will have a set of child variables that only work within the list
    - Available to all lists: `{i}`/`{index}`, `{raw}`, `{count}`
  - Separator
    - Separators all you to set text to be put between entries, but not at the end
    - Inline: `{effects, "{e:name}", ", "}`. Puts a comma between the names
    - Multiline: everything after the `=separator=` will be the separator
      - ![INSERT IMG HERE]()
  - Filter
    - Filters allow you to filter the entries before looping
    - Inline: `{effects, "{e:name}", e:level > 2}`
    - Multiline `=for: effects, e:level > 2=`
  - Separator and Filter at once, inline
    - `{effects, "{e:name}", ", ", e:level > 2}`
- Added the following to conditionals (will now refer to them as expressions)
    - You can now use `+`, `-`, `*`, `/`, `%`*(modulus)*, and `^`*(exponential)*
        - Ex: `{{x > 8+2, "X is greater than 10"}}`
        - *(Don't forget you can already wrap things in `()`s to change the order of operations)*
    - Equals (`=`/`==`) are no longer case-sensitive
    - Ternary operator `condition ? valueA ; valueB`
      - Example: `{setsleep_time, raining ? 12542 ; 12010}`
    - Added Not (`!`) operator
      - Example: `{{ !(raining), "not raining","raining"}}`
    - Added `has` operator
      - Checks to see if a list contains an entry
        - Example: `{{items has "Stone", "I have Stone"}}`
        - Can use child var to test for a specific value
          - `<list>.<child> has <value>`
          - Example: `{{items.count has 64, "I have at least 1 full stack"}}`
      - Or checks to see if a string has a string in it
        - Example `{{item:main:name has "wool", "This has wool in the name"}}`
      - Math Symbol aliases (just for the lols)
        - `∋`, `∍`, `∌` (`list ∋ element`)
        - `∈`, `∊`, `∉` (`element ∈ list`)
    - Functions (all trig uses degrees)
      - Example: `{set:test, sin(30+60)}`
      - Functions added:
        - `sin`, `cos`, `tan`, `csc`, `sec`, `cot`
        - `asin`, `acos`, `atan`, `acsc`, `asec`, `acot`
        - `round`, `ceil`, `floor`
        - `sqrt`, `abs`
    - More math symbol aliases for the lols
      - And: `⋀`, `∧`
      - Or: `⋁`, `∨`
      - Sqrt: `√(<expression>)`
- `{$<expression>}` now exists. It uses the same parsing as conditional
  - Ex: `{$ x * 10}` →  `50` (if player's x coord is 5)
  - To change the precision: `{$<precision>,<expression>}`
    - Ex: `{$2, x * 10}` → `50.00` (if player's x coord is 5)
- Expanded Profile Section
  - Width
    - Width can now be set to `max` or `-2`. The background for every line will be as wide as the longest line
    - The default behavior can now be written as `fit` or `-1`
  - Text Align
    - `==Section:TopLeft,0,0,false,fit,<align>==`
    - `left`, `right`, `center`
- Ability to disable vanilla hud elements (like hotbar or effects)
  - `==Disable: <VanillaHudElements>==`
  - Elements:
    - `health`, `hunger`, `armor`, `air`, (`status_bars` for all 4 + `horse_health`)
    - `hotbar`, `xp`, (`lower` for both and everything in `status_bars`)
    - `horse_health`, `horse_jump` (`horse` for both)
    - `chat`, `scoreboard`, `bossbar`, `subtitles`, `effects`/`status_effects`
    - `titles`, `actionbar`
- Colors
  - Putting `&` in-front of any variable will now use its number value for color
    - Ex: `&{target_block_color}`
    - Various variables have been added to take advantage of this
    - Word colors like `{red}` are now proper variables and can be used in expressions
    - Can use expressions, Ex: `&{target_block_color * -1}`
- Variables with formatting
  - Variables like `{display_name}` and `{title_msg}` now show formatting. Like if their name is red, it'll show red.
- Identifier Variables
  - Variables like `{target_block_id}` ("minecraft:stone")
  - When compared, if the id has minecraft as the namespace and you leave out the namespace, it'll still return true
    - Example: `{{target_block_id = "stone", "Is Stone", "Not Stone"}}` now works


### New Colors and Formatting
- Word versions of formatting now exists:
  - `&{bold}`, `&{italics}`, `&{underline}`, `&{obfuscated}`
  - `&{strike}`/`&{strikethrough}`, `&{reset}`
- Added Bedrock color code/names
  - `#DDD605` → `&g`, `&{minecoin_gold}`, `&{minecoin}`
  - `#E3D4D1` → `&h`, `&{material_quartz}`, `&{quartz}`
  - `#CECACA` →`&i`, `&{material_iron}`, `&{iron}`
  - `#443A3B` → `&j`, `&{material_netherite}`, `&{netherite}`
  - `#971607` → `&zm`, `&{material_redstone}`, `&{redstone}`
  - `#B4684D` → `&zn`, `&{material_copper}`, `&{copper}`
  - `#DEB12D` → `&p`, `&{material_gold}`, `&{mgold}`
  - `#47A036` → `&q`, `&{material_emerald}`, `&{emerald}`
  - `#2CBAA8` → `&s`, `&{material_diamond}`, `&{diamond}`
  - `#21497B` → `&t`, `&{material_lapis}`, `&{lapis}`
  - `#9A5CC6` → `&u`, `&{material_amethyst}`, `&{amethyst}`

### New Variable Flags
- `-sub`/`-subscript`: Numbers are replaced with their subscript counterpart
- `-sup`/`-superscript`: Numbers are replaced with their subscript counterpart
- `-r<degrees>`/`-rotate<degrees>`: Rotates Icons
  - Ex: `{item:main:icon -r45}` will rotate the icon 45 degrees
- `-hex`: Numbers are shown as hexadecimal numbers
- `-zf#`/`-zerofill#`: Pad the left with zeros when needed
  - Ex: `{x -zf3}` (5 would be 005)
- For Identifier Variables
  - `-ns`/`-namespace`: Only shows the namespace
  - `-path`: Only shows the path
- `-v#`/`-freq#`/`-frequency#`
  - Sets how often the variable updates in seconds. Doesn't work on lists
  - Ex: `-v2`, `-v0.5`, `-v1/20`
- `-rn`/`-roman`: Displays the number in Roman Numberals
  ![SCREENSHOT HERE]()
### New Themeing
- `==NiceScale: <(+/-) GUI Scale>==`
  - Since minecraft text rendering isn't great, a lot of scales look meh. However setting the scale to make in line with the GUI scale up or down does look good, but then you have to calculate what that scale is, and only works for 1 gui scale at a time. This adds a nicer way to do it.
  - `==NiceScale: +1==` with your GUI Scale set to 3 in Minecraft's settings, the profile will render as if it was set to 4.
  - `==NiceScale: 2==` will render as if it GUI Scale was set to 2.
- `==HudScale: <(+/-) GUI Scale>==`
  - Same as NiceScale, but for minecraft's vanilla hud (hotbar, scoreboard, bossbar, etc)

### List Variables
List variables will be shown without the `{}` around them, as they are intended to be used in the syntax laid out above  
Child variables will be given in the next section.
- `effects`
- `pos_effects`/`positive_effects`
- `neg_effects`/`negative_effects`
- `neu_effects`/`neutral_effects`
- `players`
- `subtitles`
- `target_block_properties`/`target_block_props`/`tbp`
- `target_block_tags`/`tbt`
- `target_fluid_properties`/`target_fluid_props`/`tfp`
- `target_fluid_tags`/`tft`
- `target_villager_offers`, `tvo`
- `item:<slot>:enchants`
- `item:<slot>:lore`
- `attributes`
- `target_entity_attributes`/`target_entity_attrs`/`teas`
- `hooked_entity_attributes`/`hooked_entity_attrs`/`heas`
- `teams`
- `item:<slot>:attributes`/`item:<slot>:attrs`
- `item:<slot>:can_destroy`, `item:<slot>:can_place_on`
- `item:<slot>:info_shown`, `item:<slot>:info_hidden`
- `items`, `hotbar_items`, `armor_items`
- `inventory_items`/`inv_items` (the 3 rows in your inventory)
- `objectives`
- `scores`, `score:<player>`
- `bossbars`
- `all_bossbars` (Singleplayer only)
- `records` (Currently playing records)
- `itag:<item_tag>`, `btag:<block_tag>`
- `mods` (List of mods, excluding libraries, child-mods, and "non-mods" like java)
- `all_root` (List of mods, excluding child-mods and "non-mods" like java)
- `all_mods` (List of mods, no exclusions)
- `resource_packs`, `disabled_resource_packs`
- `data_packs`/`datapacks`, `disabled_data_packs`/`disabled_datapacks`
- Loops
  - `loop[<start>,<end>,<step>]`
  - `loop[<start>,<end>>]` (step = 1)
  - `loop[<end>]` (start = 0, step = 1)
  - start, end, and step allows expressions
  - Example:
    - `loop[1, health, 2]`

### List Child Variables
Reminder that the `e:` and likewise can be changed with the `-pre:<prefix>` list flag.  
Example: `{effects -pre:eff, "{eff:name}"}`

`effects`, `pos_effects`, `neg_effects`, `neu_effects`
- `{e:name}`, `{e:id}`
- `{e:amp}`/`{e:amplification}` (how it's stored, 0 → 127 then -128 → -1)
- `{e:lvl}`/`{e:level}` (1 → 256)
- `{e:dur}`/`{e:duration}`, `{e:inf}`/`{e:infinite}`
- `{e:ambient}`, `{e:show_particles}`/`{e:particles}`, `{e:show_icon}`
- `{e:category}`/`{e:cat}`
- `{e:color}` (see new color feature)
- `{e:icon}`, `{e:icon_no_bg}`

`players`
- `{pl:name}`/`{pl:display_name}`, `{pl:username}`
- `{pl:id}`/`{pl:uuid}`
- `{pl:latency}`, `{pl:list_score}`
- `{pl:gamemode}`, `{pl:survival}`, `{pl:creative}`, `{pl:adventure}`, `{pl:spectator}`
- `{pl:head}` (icon)
- `{pl:team}`, `{pl:team:<team child var>}` (see `teams`)
  - Example `{pl:team:id}`

`subtitles`
- `{su:id}`, `{su:name}`
- `{su:age}`, `{su:time}`
- `{su:x}`, `{su:y}`, `{su:z}`, `{su:dist}`/`{su:distance}`
- `{su:dir}`/`{su:direction}`, `{su:left}`, `{su:right}`
- `{su:dir_yaw}`/`{su:direction_yaw}`,`{su:dir_pitch}`, `{su:direction_pitch}`
- `{su:alpha}` (allows you to control the fade, see new color feature)

`target_block_properties`, `target_fluid_properties`
- `{bp:name}`, `{bp:value}`, `{bp:type}`, `{bp:full_type}`

`target_block_tags`, `target_fluid_tags`
- `{t:name}`, `{t:id}`

`target_villager_offers`
- `{o:uses}`,`{o:max_uses}`
- `{o:special_price}`,`{o:demand_bonus}`,`{o:price_multiplier}`
- `{o:disabled}`,`{o:can_afford}`

- `{o:first:<method>}`
- `{o:first_base:<method>}`
- `{o:second:<method>}`
- `{o:result:<method>}`
  - For the 4 above: See `items` child vars, method is the same, but without the `i:`
- List Var `{o:first}`, `{o:first_base}`
- List Var `{o:second}`
- List Var `{o:result}`
  - For the 4 above: See `items` child vars

`item:<slot>:enchants`, `i:enchants`
- `{en:name}`, `{en:id}`
- `{en:level}` (like IV)
- `{en:max_level}` (max level the enchantment can be)
- `{en:full}` (like Sharpness IV)
- `{en:num}`/`{en:number}` (0 indexed)
- `{en:max_num}`, `{en:max_number}` (0 indexed)
- `{en:rarity}`

`item:<slot>:lore`
- `{lore:line}`

`attributes`, `target_entity_attributes`, `hooked_entity_attributes`  
If not in singleplayer, you will only see tracked ones
- `{at:name}`, `{at:id}`
- `{at:value}`, `{at:base_value}`, `{at:default_value}`
- `{at:tracked}` (if the server sends it to the client)
- `at:modifiers` (a list variable, see the attribute modifiers)

`at:modifiers`
- `{am:name}`, `{am:id}`
- `{am:value}`
- `{am:op}`/`{am:operatopn}` (+ (addition), ☒ (multiply base), × (multiply total))
- `{am:op_name}`, `{am:operation_name}` (Addition, Multiplication Base, Multiplication Total)

`teams`
- `{te:name}`, `{te:id}`
- `{te:color}` (see new color feature)
- `{te:friendly_fire}`, `{te:see_friendly_invis}`, `{te:collision}`
- `{te:name_tag_visibility}`/`{te:name_tag}`, `{te:death_msg_visibility}`/`{te:death_msg}`
- List Var `te:members` with child `{tm:member}` (members include offline players)
- List Var `te:online_players`/`players` (See `players` child vars)

`item:<slot>:attributes` (Item Attribute Modifiers)
- `{im:slot}` (where the item has to be for the attr to apply)
Attribute Info
- `{im:attribute}`/`{im:attr}` (attribute name)
- `{im:attribute_id}`, `{im:attr_id}`
- `{im:attribute_value}`, `{im:attr_value}`
- `{im:default_value}`, `{im:tracked}`
Attribute Modifier Info
- `{im:modifier_name}`/`{im:mod_name}`
- `{im:modifier_id}`/`{im:mod_id}`
- `{im:mod_amount}`, `{im:amount}`
- `{im:op}`, `{im:operation}` (+ (addition), ☒ (multiply base), × (multiply total))
- `{im:op_name}`, `{im:operation_name}` (Addition, Multiplication Base, Multiplication Total)

`can_destroy`, `can_place_on`
- `{cx:name}`, `{cx:id}`

`info_shown`, `info_hidden`
- `{ii:info}`

`items`, `hotbar_items`, `armor_items`, `inv_items`
- `{i:id}`, `{i:item}` (Item name)
- `{i:name}` (Custom name of the item)
- `{i:count}`, `{i:max_count}`
- `{i:dur}`/`{i:durability}`, `{i:max_dur}`, `{i:max_durability}`
- `{i:dur_per}`, `{i:durability_percentage}`
- `{i:dur_color}`, `{i:durability_color}` (See new color feature)
- `{i:unbreakable}`, `{i:repair_cost}`, `{i:hide_flags}`
- `{i:rarity}` (The name of rarity, can also be used with the new color feature)
- `{i:icon}` 
- List Var `i:enchants` (See `i:enchants` section above)
- List Var `i:lore` with child var `{lore:line}`
- List Var `i:attributes`/`i:attrs` (See Item Attribute Modifiers child vars)
- List Var `i:info_shown` and `i:info_hidden` with child `{ii:info}`
- List Var `i:can_destroy` and `i:can_place_on`
  - Children `{cx:name}`, `{cx:id}`
- List Var `tags` with children `{t:name}` and `{t:id}`

`objectives`
- `{o:name}`, `{o:id}`
- `{o:criteria}`/`{o:criterion}` (Singleplayer only)
- `{o:display_slot}`
- List Var `o:scores` (see below)
- List Var `o:online_scores` (see below)

`o:scores`, `o:online_scores`, `objective:<objective_id>:scores`, `objective:<objective_id>:online_scores`
- `{os:name}`/`{os:holder}`, `{os:display}`/`{os:display_name}`
- `{os:score}`/`{os:value}`

`scores`, `score:<player>`
- `{ss:name}` (objective name)
- `{ss:id}` (objective id)
- `{ss:criteria}`/`{ss:criterion}` (Singleplayer only)
- `{ss:display_slot}`
- `{ss:score}`/`{ss:value}` (player's score for this objective)

`records`
- `{r:name}`, `{r:id}`
- `{r:elapsed}`, `{elapsed_per}`/`{elapsed_percentage}`
- `{r:length}`, `{r:remaining}`
- `{r:icon}`

`bossbars`, `all_bossbars`
- `{bb:name}`, `{bb:uuid}`
- `{bb:id}` (Singpleplayer only, and only for command made bossbars),
- `{bb:percent}`/`{bb:per}`/`{bb:value}`
- `{bb:darken_sky}`, `{bb:dragon_music}`, `{bb:thickens_fog}`
- `{bb:style}`, `{bb:color}`, `{bb:text_color}`
- `{bb:enabled}`/`{bb:visible}` (Singleplayer only)
- `{bb:icon}`
- List Var `bb:players` (See `players` child vars)
  - A list of players that can see the bossbar, Singleplayer only

`itag:<item_tag>`, `btag:<block_tag>`
- `{t:name}`, `{t:id}`, `{t:icon}`

`mods`, `all_root_mods`, `all_mods`, `m:parent`, `m:children`
- `{m:name}`, `{m:id}`, `{m:version}`, `{m:hash}`
- `{m:summary}`, `{m:desc}`/`{m:description}`
- `{m:icon}`
- If the mod is a library mod, client mod, etc:
  - `{m:library}`, `{m:client}`, `{m:deprecated}`, `{m:patchwork}`, `{m:from_modpack}`, `{m:minecraft}`
- List Var `m:badges` (See section below)
- List Var `m:authors` with child var `{ma:author}`
- List Var `m:contributors` with child var `{mc:contributor}`
- List Var `m:credits` with child var `{mc:credit}`
- List Var `m:licenses` with child var `{ml:license}`
- List Var `m:children` same child vars
- List Var `m:parent` same child vars (only 1 entry)
- `{m:parent:<method>}` (method is the same as child, but without the `m:`)

`m:badges`
- `{mb:name}`
- `{mb:outline_color}`, `{mb:fill_color}`
- `{mb:icon}`

`resource_packs`, `disabled_resource_packs`, `datapacks`, `disabled_datapacks`
- `{p:name}`, `{p:id}`, `{p:version}`
- `{p:desc}`/`{p:description}`
- `{p:compatible}`, `{p:always_enabled}`, `{p:pinned}`
- `{p:icon}`

### New Variables
- `{is_pressed:<keybind>}`
  - Example: `{is_pressed:forward}`
- `{toggle:<name>}`
  - Example: `{toggle:test}`
  - Set the keybind for the toggle in the new config screen.
- Get/Set Values
  - `{set:<names>, <expression>}`
    - `<expression>` is the same type as used in conditionals and `{$<expression>}`
    - Runs the expression on **set**
  - `{get:<name>}`
- Macros
  - `{setmacro:<name>, <expression>}`
    - `<expression>` is the same type as used in conditionals and `{$<expression>}`
    - Runs the expression on **get**, not set
  - `{setmacro:<name>, "stuff here"}`
    - `"stuff here"` the same as `{{<condition>, "stuff here if true"}}`
    - Example: `{setmacro:cords, "&c{x} &a{y} &b{z}"}`
  - `{getmacro:<name>}`
- `{slime_chunk:<seed>}`
  - You should still use `{slime_chunk}` in singleplayer
- Target Block and Target Fluid:
  - `{target_block_color}`/`{target_color}`/`{tbc}`
  - `{target_fluid_color}`/`{tfc}`
  - `{target_block_icon}`/`{target_icon}`/ `{tbicon}`
  - `{target_fluid_icon}`/ `{tficon}`
- Information about the entity you last hit:
  - `{last_hit}`/`{lh}` (Pig, Player)
  - `{last_hit_id}`/`{lhi}` (minecraft:pig, minecraft:player)
  - `{last_hit_name}`/`{lhn}` (Pig, Notch)
  - `{last_hit_uuid}`/`{lhu}` (b354c639-b534-48fa-b4c3-48192dba8dd3)
  - `{last_hit_distance}`/`{lhd}` (Distance between you and the entity at the time you hit)
- Information about the entity you are riding:
  - `{ve}`/`{vehicle}` (Pig, Player)
  - `{vei}`/`{vehicle_id}` (minecraft:pig, minecraft:player)
  - `{ven}`/`{vehicle_name}` (Pig, Notch, (Pig with a nametag 'Pet') Pet)
  - `{veu}`/`{vehicle_uuid}` (b354c639-b534-48fa-b4c3-48192dba8dd3)
  - `{veh}`/`{vehicle_health}`, `{vea}`/`{vehicle_armor}`, `{vemh}`/`{vehicle_max_health}`
  - `{vhj}`/`{horse_jump}`, `{vha}`/`{horse_armor}` (name of the armor)
- Added Hooked Entity yaw/pitch
  - `{hedy}`/`{hooked_entity_direction_yaw}`
  - `{hedp}`/`{hooked_entity_direction_pitch}`
- Information about the villager you are looking at
  - `{tvb}`/`{target_villager_biome}`
  - `{tvlw}`/`{target_villager_level_word}`, `{tvl}`/`{target_villager_level}`
  - `{tve}`/`{target_villager_xp}`, `{tven}`/`{target_villager_xp_needed}`
  - `{tveb}`/`{target_villager_xp_bar}`
- Added plural variants to time variables
  - People, including myself, kept adding an s to the end, so why not allow it
  - `{seconds}`, `{minutes}`, `{hours}`, `{hours12}`, `{hours24}`
- Added individual real time variables
  - `{realtime:<format>}` is still recommend for showing time, these are mainly for use in expressions
  - `{real_year}`, `{real_month}`, `{real_day}`
  - `{real_day_of_week}`/`{real_dow}`, `{real_day_of_year}`/`{real_doy}`
  - `{real_hour}`/`{real_hour12}`, `{real_hour24}`
  - `{real_minute}`, `{real_second}`, `{real_ms}`
  - `{real_am}`, `{real_pm}`
- Added Profile Variables
  - `{profile_name}`, `{profile_errors}`, `{profile_keybind}`, `{profile_in_cycle}`
- Title and Actionbar Variables
  - `{title_msg}`/`{title}`, `{subtitle_msg}`/`{subtitle}` (not to be confused with audio subtitles)
  - `{actionbar_msg}`/`{actionbar}`
  - `{title_remaining}`, `{actionbar_remaining}`
- Added Variables for the game's current Resource Pack and Datapack version
  - `{rp_version}`/`{resource_pack_version}`
  - `{dp_version}`, `{data_pack_version}`/`{datapack_version}`
- Player's Current Team
  - `{pteam:<method>}`/`{player_team:<method>}`
  - Existing `{team}` (no method) now alias of `{pteam}` (no method) and `{player_team}` (no method)
  - Methods:
    - `name`, `id`
    - `color` (see new color feature)
    - `friendly_fire`, `see_friendly_invis`, `collision`
    - `name_tag_visibility`/`name_tag`, `death_msg_visibility`/`death_msg`
    - List Var `members` with child `{tm:member}` (members include offline players)
    - List Var `online_players`/`players` (See `players` child vars)
- (Non-list) Attribute Variables
  - You (the player): `{attribute:<attr>:<method>}`
  - Target Entity:
    - `{target_entity_attribute:<attr>:<method>}`
    - `{target_entity_attr:<attr>:<method>}`
    - `{tea:<attr>:<method>}`
  - Hooked Entity:
    - `{hooked_entity_attribute:<attr>:<method>}`
    - `{hooked_entity_attr:<attr>:<method>}`
    - `{hea:<attr>:<method>}`
  - Methods:
    - `name`, `id`
    - `value`, `base_value`, `default_value`
    - `tracked` (if the server sends it to the client)
    - `modifiers` (a list variable, see the attribute modifiers)
- (Non-list) Team Variables
  - `{team:<team>:<method>}`
  - Methods:
    - `name`, `id`
    - `color` (see new color feature)
    - `friendly_fire`, `see_friendly_invis`, `collision`
    - `name_tag_visibility`/`name_tag`, `death_msg_visibility`/`death_msg`
    - List `members` with child `{member}` (members include offline players)
    - List `online_players`/`players` (See `players` child vars)
- More methods for `{item:<slot>:<method>}`
  - `unbreakable`, `repair_cost`, `hide_flags`
  - List `attributes`/`attrs` (See Item Attribute Modifiers child vars)
  - List `info_shown` and `info_hidden` with child `{ii:info}`
  - List `can_destroy` and `can_place_on`
    - Children `{cx:name}`, `{cx:id}`
  - List `tags` with children `{t:name}` and `{t:id}`
- (Non-list) Objective Variables
  - `{objective:<objective_id>:<method>}`
  - Methods:
    - `name`, `id`
    - `criteria`/`criterion` (Singleplayer only)
    - `display_slot`
    - List `scores` (See child variable section above)
    - List `online_scores` (See child variable section above)
- (Non-list) Score Variable
  - `{score:<player>:<objective_id>}`
  - Gets the score the player has for that objective
- (Non-list) Bossbar Variables
  - `{bossbar:<name|uuid|id>:<method>}`
    - id singleplayer only, and only ones made by commands
  - Methods:
    - `name`, `uuid`
    - `id` (Singpleplayer only),
    - `percent`/`per`/`value`
    - `darken_sky`, `dragon_music`, `thickens_fog`
    - `style`, `color`, `text_color`
    - `enabled`/`visible` (Singleplayer only)
    - `icon`
    - List `players` (See `players` child vars)
      - A list of players that can see the bossbar, Singleplayer only
- (Non-list) Mod Variables
  - `{mod:<id>:<method>}`
  - `{mod:<id>:parent:<method>}`
  - Methods:
    - `name`, `id`, `version`, `hash`
    - `summary`, `desc`/`description`
    - `icon`
    - If the mod is a library mod, client mod, etc:
          - `library`, `client`, `deprecated`, `patchwork`, `from_modpack`, `minecraft`
    - List `authors` with child var `{ma:author}`
    - List `contributors` with child var `{mc:contributor}`
    - List `credits` with child var `{mc:credit}`
    - List `licenses` with child var `{ml:license}`
    - List `children`. See `mods` child vars
    - List `parent`. See `mods` child vars
    - List `badges`. See `m:badges` child vars
- (Non-list) Resource Pack / Datapack Variables:
  - `{resource_pack:<id>:<method>}`
  - `{data_pack:<id>:<method>}`/`{datapack:<id>:<method>}`
  - Methods:
    - `name`, `id`, `version`
    - `desc`/`description`
    - `compatible`, `always_enabled`, `pinned`
    - `icon`
- Progress Bar
  - `{bar, <value>, <max_value>}`
  - `{bar:<style>, <value>, <max_value>}`
  - `{bar, <value>, <max_value>, <flags>}`
  - Styles:
    - `xp`/`experience`, `jump`/`horse`
    - `villager`/`villager_green`, `villager_white`
    - `pink`, `red`, `blue`, `green`, `yellow`, `purple`, `white`
    - `6`, `10`, `12`, `20` (notches)
    - `pink6`, `red12`, etc
  - Example:
    - `{bar:red20, y + 64, 384}`
- Timer Variable
  - `{timer[<end>, <interval>]}`
  - `{timer[<end>]}` (interval = 1)
  - end and interval allows expressions
  - Example:
    - `{timer[100, 1/health]}`
    - Counts to 100. counts faster if you have less health
- F3 Parity:
  - Client/Server Chunk Variables (don't ask me what the actual numbers mean)
    - `{ccw1}`, `{ccw2}`, `{cce1}`, `{cce2}`, `{cce3}`
    - `{scw1}`, `{sce1}`, `{sce2}`, `{sce3}`, `{sce4}`, `{sce5}`, `{sce6}`, `{sce7}`
  - Display/GPU/GL
    - `{gpu_vendor}`, `{gl_version}`, `{gpu_driver}`
  - Tick related stuff
    - Minecraft added the ability to change and freeze the tick rate in a recent version
      - `{ms_per_tick}`
      - `{is_tick_sprinting}`/`{tick_sprinting}`
      - `{is_tick_frozen}`/`{tick_frozen}`
      - `{is_tick_stepping}`/`{tick_stepping}`
  - `{memory_allocation_rate}`
  - Fabric's `{active_renderer}`
- Math constants:
  - `{e}`, `{pi}`/`{π}`, `{tau}`/`{τ}`, `{phi}`/`{φ}`/`{golden_ratio}`

### Variable Changes
- `{tps}` and `{ms_ticks}`/`{ticks_ms}` are now estimated when on servers!!!
- `{ms_ticks}` no longer bottoms out at 50ms in singleplayer
- Improvements to `{space:#}`
  - You can use the expression `{space:x + 5}`
  - You can now get the same with has elements would, like
    - `{space:"Name: {name}"}`
- Old record variables now show data for the closest playing record instead of the newest
  - `{record_name}`, `{record_id}`, etc
- Texture Icons
  - `{icon:<path>}` / `{icon:<path> <flags>}`
    - Path can now end in `.png`. You don't need it, but it'll work now
  - Changes to icons with u,v,w,h
    - No longer broken (and now will have proper docs because of this)
    - Old: `{icon:<path>,<u>,<v>,<w>,<h> <flags>}`
    - New: `{icon:<path> <flags>, <u>, <v>, <w>, <h>, <width>, <height>}`
    - New: `{icon:<path> <flags>, <u>, <v>, <w>, <h>, <height>}`
      - All parts are optional
      - Examples:
        - `{icon:<path>, 0, 0}` (w,h,width,height are default)
        - `{icon:<path>, 0, 0, 16, 16, 20}` (width is default)
    - Parts:
      - `width`: how wide the icon renders
      - `height`: how tall the icon renders
      - [INSERT UVWH PICTURE HERE]()
- `{gpu}` now shows the same way as it does in F3
  - This is less desirable for NVIDIA gpus, but it was broken for AMD's 

### Text and Identifier Variables
These variables are support showing their formatting:
  - `{name}`/`{display_name}`
  - `{ten}`/`{target_entity_name}`
  - `{lhn}`/`{last_hit_name}`
  - `{hen}`/`{hooked_entity_name}`
  - `{ven}`/`{vehicle_name}`/`{vehicle_entity_name}`
  - `{vha}`/`{horse_armor}`/`{vehicle_horse_armor}`
  - `{team}`, `{team_name}`
  - `{record_name}`
  - `{title}`/`{title_msg}`
  - `{subtitle}`/`{subtitle_msg}`
  - `{actionbar}`/`{actionbar_msg}`
  - All the ones already stated in the list child var section

These variables are now Identifier variables:
  - `{tei}`/`{target_entity_id}`
  - `{lhi}`/`{last_hit_id}`
  - `{hei}`/`{hooked_entity_id}`
  - `{vei}`/`{vehicle_id}`/`{vehicle_entity_id}`
  - `{dimension_id}`, `{biome_id}`
  - `{music_id}`, `{record_id}`
  - All the ones already stated in the list child var section


### Other Changes
- Profiles will reload on resource reload
- If the error screen is opened via the keybind, there's now a button that go to profiles (new config screen)

### New Registry System
- Breaks old addon mods (the non-existent ones)
- `CustomHudRegistry.registerElement("name", (flags, context) -> <ELEMENT>)`
  - Pass flags into elements that take flags, and wrap string variables in `Flags.wrap(<ELEMENT>, flags)`
- `CustomHudRegistry.registerParser("id", (str, context) -> <ELEMENT>)`
- `CustomHudRegistry.registerList("name", "default prefix", () -> <LIST>, <ATTRIBUTER>)`
- Also unregister/has for all 3 kinds of registers

### Fixes
- If lines only contains conditionals (and now set/setmacro) that produces nothing, the lines are properly skipped again
- Built-in-fonts now work again (also fixes enchanting table text)
- No longer crashes when default font is replaced with a tff font
- Removed sodium/iris variables so the mod no longer breaks when they make breaking changes
  - These will return as addon mods
- Fixes to icons and rich item icons
- No longer crashes when using BooleanSupplierElement before minecraft's options are loaded
- Fixed text positioning when line spacing is 0