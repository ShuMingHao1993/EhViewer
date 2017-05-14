/*
 * Copyright 2017 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.ehviewer.view;

/*
 * Created by Hippo on 2/8/2017.
 */

import android.support.annotation.IntDef;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import com.hippo.ehviewer.R;
import com.hippo.ehviewer.presenter.PresenterInterface;
import com.hippo.ehviewer.scene.EhvScene;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@code ToolbarView} shows a Toolbar at top.
 */
public abstract class ToolbarView<P extends PresenterInterface, S extends EhvScene>
    extends StatusBarView<P, S> {

  @IntDef({NAVIGATION_TYPE_MENU, NAVIGATION_TYPE_RETURN})
  @Retention(RetentionPolicy.SOURCE)
  public @interface NavigationType {}

  public static final int NAVIGATION_TYPE_MENU = 0;
  public static final int NAVIGATION_TYPE_RETURN = 1;

  private Toolbar toolbar;

  @NonNull
  @Override
  protected View onCreateStatusBarContent(LayoutInflater inflater, ViewGroup parent) {
    View view = inflater.inflate(R.layout.view_toolbar, parent, false);

    toolbar = (Toolbar) view.findViewById(R.id.toolbar);

    DrawerArrowDrawable drawable = new DrawerArrowDrawable(getEhvActivity());
    drawable.setDirection(DrawerArrowDrawable.ARROW_DIRECTION_LEFT);
    toolbar.setNavigationIcon(drawable);
    switch (getNavigationType()) {
      case NAVIGATION_TYPE_MENU:
        drawable.setProgress(0.0f);
        toolbar.setNavigationOnClickListener(v -> getEhvActivity().openLeftDrawer());
        break;
      case NAVIGATION_TYPE_RETURN:
        drawable.setProgress(1.0f);
        toolbar.setNavigationOnClickListener(v -> getEhvScene().pop());
        break;
    }

    ViewGroup container = (ViewGroup) view.findViewById(R.id.toolbar_content_container);
    View content = onCreateToolbarContent(inflater, container);
    container.addView(content);

    return view;
  }

  /**
   * Creates content view for this {@code ToolbarView}.
   */
  protected abstract View onCreateToolbarContent(LayoutInflater inflater, ViewGroup parent);

  /**
   * {@link #NAVIGATION_TYPE_RETURN}: arrow, back.
   * {@link #NAVIGATION_TYPE_MENU}: menu, open menu.
   * <p>
   * Override it to change navigation type.
   * <p>
   * Default: {@link #NAVIGATION_TYPE_RETURN}.
   */
  @NavigationType
  protected int getNavigationType() {
    return NAVIGATION_TYPE_RETURN;
  }

  /**
   * Return the Toolbar.
   */
  public Toolbar getToolbar() {
    return toolbar;
  }

  /**
   * Set title for the {@code Toolbar}.
   */
  public void setTitle(int resId) {
    toolbar.setTitle(resId);
  }

  /**
   * Set title for the {@code Toolbar}.
   */
  public void setTitle(CharSequence title) {
    toolbar.setTitle(title);
  }

  /**
   * Sets menu for the {@code Toolbar}.
   */
  public void setMenu(@MenuRes int resId, Toolbar.OnMenuItemClickListener listener) {
    toolbar.inflateMenu(resId);
    toolbar.setOnMenuItemClickListener(listener);
  }

  /**
   * Gets menu of the {@code Toolbar}.
   */
  public Menu getMenu() {
    return toolbar.getMenu();
  }
}