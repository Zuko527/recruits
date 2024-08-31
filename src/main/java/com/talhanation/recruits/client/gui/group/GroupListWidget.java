package com.talhanation.recruits.client.gui.group;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

import java.util.List;
public class GroupListWidget extends ObjectSelectionList<GroupListWidget.GroupEntry> {
    private final int listWidth;
    private final GroupManageScreen parent;
    private List<RecruitsGroup> groups;

    public GroupListWidget(GroupManageScreen parent, int listWidth, int top, int bottom, List<RecruitsGroup> groups) {
        super(parent.getMinecraft(), listWidth, parent.height, top, bottom, parent.getFont().lineHeight * 2 + 12);
        this.parent = parent;
        this.listWidth = listWidth;
        this.groups = groups;
        this.refreshList();
    }

    public void removeGroup(GroupEntry entry){
        this.removeEntry(entry);
    }
    @Override
    protected int getScrollbarPosition() {
        return this.x0 + this.listWidth - 5; // Adjust scrollbar position
    }

    @Override
    public int getRowWidth() {
        return this.listWidth;
    }

    public void refreshList() {
        this.clearEntries();
        for (RecruitsGroup groupInfo : groups) {
            this.addEntry(new GroupEntry(groupInfo, this.parent));
        }
    }

    public class GroupEntry extends ObjectSelectionList.Entry<GroupEntry> {
        private final RecruitsGroup group;
        private final GroupManageScreen parent;

        GroupEntry(RecruitsGroup group, GroupManageScreen parent) {
            this.group = group;
            this.parent = parent;
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select", group.getName());
        }

        public void renderBack(PoseStack guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            // Grauen Hintergrund rendern
            fill(guiGraphics, left, top, left + entryWidth, top + entryHeight, 0xFF404040);
        }

        @Override
        public void render(PoseStack guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            Font font = this.parent.getFont();
            Component name = Component.literal(group.getName());
            //Component id = Component.literal("ID: " + group.getId());
            Component count = Component.literal("Count: " + group.getCount());

            font.draw(guiGraphics, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name, listWidth))), left + 3, top + 2, 0xFFFFFF);
            //guiGraphics.drawString(font, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(id, listWidth))), left + 3, top + 2 + font.lineHeight, 0xCCCCCC, false);
            font.draw(guiGraphics, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(count, listWidth))), left + 3, top + 2 + font.lineHeight, 0xCCCCCC);

        }

        private int calculateTotalCount() {
            return groups.stream().mapToInt(RecruitsGroup::getCount).sum();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                parent.setSelected(this);
                GroupListWidget.this.setSelected(this);
                return true;
            }
            return false;
        }

        public RecruitsGroup getGroup() {
            return group;
        }
    }
}
