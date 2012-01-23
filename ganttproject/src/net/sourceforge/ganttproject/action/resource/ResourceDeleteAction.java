/*
GanttProject is an opensource project management tool.
Copyright (C) 2011 GanttProject Team

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
package net.sourceforge.ganttproject.action.resource;

import java.awt.event.ActionEvent;


import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.action.GPAction;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.UIFacade.Choice;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.resource.ResourceContext;
import net.sourceforge.ganttproject.util.StringUtils;

/**
 * Action for deleting resources
 */
public class ResourceDeleteAction extends ResourceAction {
    private final UIFacade myUIFacade;

    private final ResourceContext myContext;

    private GanttProject myProjectFrame;

    public ResourceDeleteAction(HumanResourceManager hrManager, ResourceContext context, GanttProject projectFrame,
            UIFacade uiFacade) {
        this(hrManager, context, projectFrame, uiFacade, IconSize.MENU);
    }

    private ResourceDeleteAction(HumanResourceManager hrManager, ResourceContext context, GanttProject projectFrame,
            UIFacade uiFacade, IconSize size) {
        super("resource.delete", hrManager, size);
        myUIFacade = uiFacade;
        myProjectFrame = projectFrame;
        myContext = context;
    }

    @Override
    public GPAction withIcon(IconSize size) {
        return new ResourceDeleteAction(getManager(), myContext, myProjectFrame, myUIFacade, size);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        final HumanResource[] selectedResources = myContext.getResources();
        if (selectedResources.length > 0) {
            final String message = getI18n("msg6") + " " + StringUtils.getDisplayNames(selectedResources) + "?";
            final String title = getI18n("question");
            Choice choice = myUIFacade.showConfirmationDialog(message, title);
            if (choice == Choice.YES) {
                myUIFacade.getUndoManager().undoableEdit(getLocalizedDescription(), new Runnable() {
                    @Override
                    public void run() {
                        deleteResources(selectedResources);
                        myProjectFrame.repaint2();
                    }
                });
            }
        }
    }

    private void deleteResources(HumanResource[] resources) {
        for (HumanResource resource : resources) {
            resource.delete();
        }
    }

    @Override
    protected String getIconFilePrefix() {
        return "delete_";
    }
}