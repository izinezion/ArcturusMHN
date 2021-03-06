package com.eu.habbo.essentials.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.commands.Command;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.items.Item;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SetFurnitureCommand extends Command
{
    public SetFurnitureCommand(String permission, String[] keys)
    {
        super(permission, keys);
    }

    @Override
    public boolean handle(GameClient gameClient, String[] strings) throws Exception
    {
        Room room = gameClient.getHabbo().getHabboInfo().getCurrentRoom();
        HabboItem item = room.getTopItemAt(gameClient.getHabbo().getRoomUnit().getX(), gameClient.getHabbo().getRoomUnit().getY());

        if (item == null)
        {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("essentials.cmd_set.notfound"));
            return false;
        }

        if (strings.length >= 2)
        {
            if (strings[1].equalsIgnoreCase("info"))
            {
                String message = "Opções do SET\r\n";
                message += "Use: :set <item_id> <opcao> <valor>\r\n";
                message += "width - Número (Não decimal)\r";
                message += "length - Número (Não decimal)\r";
                message += "height - Número (Decimals)\r";
                message += "stack - true/false/1/0\r";
                message += "walk - true/false/1/0\r";
                message += "sit - true/false/1/0\r";
                message += "lay - true/false/1/0\r";
                message += "states - Número (Não decimal)\r";
                message += "vending - iditem (caso mais bebidas: iditem1,iditem2 (sem espaço com virgula))\r";
                message += "interaction - InteractionType (Caso não souber, use 'default' sem aspas)\r";
                message += "multiheight - altura1;altura2;altura3 (sem espaço com ponto e virgula)\r";
                message += "effect - ID do efeito\r";
                message += "get info - Exibe informações do mobi\r";
                gameClient.getHabbo().alert(new String[]{message});
            }

            if (strings[1].equalsIgnoreCase("get"))
            {
                String message = "item name: "+ item.getBaseItem().getName() +"\n";
                message += "width: "+ item.getBaseItem().getWidth() +"\r";
                message += "length: "+ item.getBaseItem().getLength() +"\r";
                message += "height: "+ item.getBaseItem().getHeight() +"\r";
                message += "stack: "+ item.getBaseItem().allowStack() +"\r";
                message += "walk: "+ item.getBaseItem().allowWalk() +"\r";
                message += "sit: "+ item.getBaseItem().allowSit() +"\r";
                message += "lay: "+ item.getBaseItem().allowLay() +"\r";
                message += "states: "+ item.getBaseItem().getStateCount() +"\r";
                message += "vending: "+ item.getBaseItem().getVendingItems() +"\r";
                message += "interaction: "+ item.getBaseItem().getInteractionType().getName() +"\r";
                message += "effect - F: "+ item.getBaseItem().getEffectF() +" - M: "+ item.getBaseItem().getEffectM() +"\r";
                gameClient.getHabbo().alert(new String[]{message});
            }
        }

        if (strings.length >= 2)
        {
            try
            {
                handleColumn(item.getBaseItem(), strings[1], strings[2]);
            }
            catch (Exception e)
            {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("essentials.cmd_set.wrong"));
                return true;
            }
        }

        gameClient.getHabbo().whisper(Emulator.getTexts().getValue("essentials.cmd_set.succes"));

        return true;
    }

    private void handleColumn(Item item, String column, String value)
    {
        try(Connection connection = Emulator.getDatabase().getDataSource().getConnection())
        {
            switch (column)
            {
                case "width":
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET width = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setInt(1, Integer.valueOf(value));
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    break;
                case "length":
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET length = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setInt(1, Integer.valueOf(value));
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    break;
                case "height":
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET stack_height = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setDouble(1, Double.valueOf(value));
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    break;
                case "stack":
                    value = value.replace("1", "true").replace("0", "false");
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET allow_stack = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setString(1, Boolean.valueOf(value) ? "1" : "0");
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    break;
                case "walk":
                    value = value.replace("1", "true").replace("0", "false");
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET allow_walk = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setString(1, Boolean.valueOf(value) ? "1" : "0");
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    break;
                case "sit":
                    value = value.replace("1", "true").replace("0", "false");
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET allow_sit = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setString(1, Boolean.valueOf(value) ? "1" : "0");
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    break;
                case "lay":
                    value = value.replace("1", "true").replace("0", "false");
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET allow_lay = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setString(1, Boolean.valueOf(value) ? "1" : "0");
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    break;
                case "states":
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET interaction_modes_count = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setInt(1, Integer.valueOf(value));
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    break;
                case "interaction":
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET interaction_type = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setString(1, String.valueOf(value));
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    break;
                case "vending":
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET vending_ids = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setString(1, String.valueOf(value));
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    if(value != "0") {
                        try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET interaction_type = ? WHERE id = ? LIMIT 1")) {
                            statement.setString(1, "vendingmachine");
                            statement.setInt(2, item.getId());
                            statement.execute();
                        }
                    }
                    break;
                case "multiheight":
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET multiheight = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setString(1, String.valueOf(value));
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    if(value != "0") {
                        try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET interaction_type = ? WHERE id = ? LIMIT 1")) {
                            statement.setString(1, "multiheight");
                            statement.setInt(2, item.getId());
                            statement.execute();
                        }
                    }
                    break;
                case "effect":
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET effect_id_male = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setInt(1, Integer.valueOf(value));
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE items_base SET effect_id_female = ? WHERE id = ? LIMIT 1"))
                    {
                        statement.setInt(1, Integer.valueOf(value));
                        statement.setInt(2, item.getId());
                        statement.execute();
                    }
                    break;
                default: return;
            }

            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM items_base WHERE id = ? LIMIT 1"))
            {
                statement.setInt(1, item.getId());
                try (ResultSet set = statement.executeQuery())
                {
                    if (set.next())
                    {
                        item.update(set);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            Emulator.getLogging().logSQLException(e);
        }
    }
}