## WIP CustomHud v4 Changelog

### Language Features
- Added arithmetic to conditonals (will now refer to them as expressions)
    - You can now use `+`, `-`, `*`, `/`, `%`*(modulus)*, and `^`*(exponential)*
        - Ex: `{{x > 8+2, "X is greater than 10"}}`
        - *(Don't forget you can already wrap things in `()`s to change the order of operations)*
    - `{$<expression>}` now exists. It uses the same parsing as conditional
      - Ex: `{$ x * 10}` →  `50` (if player's x coord is 5)
      - To change the precision: `{$<precision>,<expression>}`
        - Ex: `{$2, x * 10}` → `50.00` (if player's x coord is 5)
- Added List Variable Support
    -  `{<list>}` alone will just show how many there are
    - Example: `{effects}` → 3 (assuming the player has 3 status effects)
    - Inline: `{<list>, "stuff here"}`
        - Example: `{effects, "{index} - {icon} {name}\n"}`
    - Multiline: `=for:<list>=` ends with `=endfor=`
    - Each list will have a set of child variables that only work within the list
      - Available to all lists: `{i}`/`{index}`, `{raw}`, `{count}`
- Expanded Profile Section
  - Width can now be set to `max` or `-2`. The background for every line will be as wide as the longest line
  - The default behavior can now be written as `fit` or `-1`

### New Variable Flags
- `-sub`/`-subscript`: Numbers are replaced with their subscript counterpart
- `-sup`/`-superscript`: Numbers are replaced with their subscript counterpart

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
- `scores`

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
- `{su:alpha}` (see new color feature)

`target_block_properties`, `target_fluid_properties`
- `{bp:name}`, `{bp:value}`, `{bp:type}`, `{bp:full_type}`

`target_block_tags`, `target_fluid_tags`
- `{t:name}`, `{t:id}`

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
- `{i:unbreakable}`, `{i:repair_cost}`, `{i:hide_flags}`, `{i:rarity}`
- List Var `i:enchants` (See `i:enchants` section above)
- List Var `i:lore` with child var `{lore:line}`
- List Var `i:attributes`/`i:attrs` (See Item Attribute Modifiers child vars)
- List Var `i:info_shown` and `i:info_hidden` with child `{ii:info}`
- List Var `i:can_destroy` and `i:can_place_on`
  - Children `{cx:name}`, `{cx:id}`
- List Var `tags` with children `{t:name}` and `{t:id}`

`objectives`

`scores`

### New Variables
- Information about the entity you last hit
    - `{last_hit}`/`{lh}` (Pig, Player)
    - `{last_hit_id}`/`{lhi}` (minecraft:pig, minecraft:player)
    - `{last_hit_name}`/`{lhn}` (Pig, Notch)
    - `{last_hit_uuid}`/`{lhu}` (b354c639-b534-48fa-b4c3-48192dba8dd3)
    - `{last_hit_distance}`/`{lhd}` (Distance between you and the entity at the time you hit)
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
    - List Var `members` with child `{member}` (members include offline players)
    - List Var `online_players`/`players` (See `players` child vars)
- More methods for `{item:<slot>:<method>}`
  - `unbreakable`, `repair_cost`, `hide_flags`
  - List Var `attributes`/`attrs` (See Item Attribute Modifiers child vars)
  - List Var `info_shown` and `info_hidden` with child `{ii:info}`
  - List Var `can_destroy` and `can_place_on`
    - Children `{cx:name}`, `{cx:id}`
  - List Var `tags` with children `{t:name}` and `{t:id}`

### Fixes
- If lines only contains conditionals (and now set/setmacro) that produces nothing, the lines are properly skipped again
- Built-in-fonts now work again (also fixes enchanting table text)
- No longer crashes when default font is replaced with a tff font
- Removed sodium/iris variables so the mod no longer breaks when they make breaking changes
  - These will return as addon mods
- 