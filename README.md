# stomui

This library is a very flexible component-based inventory framework for the minestom server framework.

It provides many examples and tries to stay up-to-date.

- [Explore examples](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views)
- [Maven](https://mvnrepository.com/artifact/eu.koboo/stomui)
- [Found a bug?](https://github.com/Koboo/stomui/issues)
- [License](LICENSE)
- [Documentation](https://github.com/Koboo/stomui/wiki/Documentation)

## FAQ

<details>
<summary>Can I open an inventory to multiple Players?</summary>
Yes and no. The actual inventories of Minestom are not really visible and backed by a PlayerView.
Every player gets an own PlayerView, by opening a ViewComponent, but you could open one ViewComponents
instance to multiple players, sharing the logic between multiple PlayerViews.
</details>

<br>

<details>
<summary>Do I have to use multiple ViewComponents?</summary>
No, you can create your inventories with only one ViewProvider. You are not forced
to use multiple ViewComponents, but it makes reusing logic within a ViewComponent easier.
</details>

<br>

<details>
<summary>Do you support async adding and removing in pagination?</summary>
Yes, the pagination is very flexible. There are several examples on how to use it. Just look up the
examples below.
</details>

## Documentation

Unfortunately, the actual documentation is still very small, but available in the [Wiki](https://github.com/Koboo/stomui/wiki/Documentation).

## Examples

To understand the usage of `stomui` in detail you can check out the examples.

#### Simple examples
- [SimpleExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/SimpleExampleProvider.java)
  - Shows most of the commonly used `ViewProvider`/`ViewComponent` and `ViewItem` apis and methods.

#### Advanced examples
- [AllowInteractionExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/other/AllowInteractionExampleProvider.java)
  - Shows how to allow clicks on specific slots using ``Flag`` and `ViewItem` api.
- [AnnotatedTabExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/other/AnnotatedTabExampleProvider.java)
  - Shows how to use annotations to render and modify `PrebuiltItem` and listen to state changes.
- [AnvilInputExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/other/AnvilInputExampleProvider.java)
  - Heavy simplified example, which shows how to get the text input from an anvil inventory.

#### Pagination
- [PageableExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/pagination/PageableExampleProvider.java)
  - Shows how to use pagination by `ViewRegistry#pageable(...)` and the `ViewComponent` hierarchy, by adding children. (Normal page)
- [AsyncPageableExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/pagination/AsyncPageableExampleProvider.java)
    - Shows how to use pagination by `ViewRegistry#pageable(...)` (Asynchronous pagination).
- [ScrollableHorizontalExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/pagination/ScrollableHorizontalExampleProvider.java)
    - Shows how to use pagination by `ViewRegistry#scrollable(...)` and the `ViewComponent` hierarchy, by adding children. (Horizontal pattern)
- [ScrollableVerticalExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/pagination/ScrollableVerticalExampleProvider.java)
    - Shows how to use pagination by `ViewRegistry#scrollable(...)` and the `ViewComponent` hierarchy, by adding children. (Vertical pattern)

#### Inventory swapping and history
- [SwitchParentProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/switching/SwitchParentProvider.java)
- [SwitchOneExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/switching/SwitchOneExampleProvider.java)
- [SwitchTwoExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/switching/SwitchTwoExampleProvider.java)
  - Shows how to switch inventory between `SwitchOneExampleProvider` and `SwitchTwoExampleProvider`.
  - Explains the difference between `navigation`- and `open`-Actions.
- [ViewHistoryExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/switching/ViewHistoryExampleProvider.java)
  - Show how to use the builtin `PlayerView` history, to avoid reopening already opened inventories.
  - Works like a browser history with `back`- and `forward`-Actions.

#### Extraordinary examples
- [SearchExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/search/SearchExampleProvider.java)
  - Pagination example, which shows how to implement a searching inventory using the anvil search input.
- [TwoPlayerExampleProvider](/examples/src/main/java/eu/koboo/minestom/examples/stomui/views/multiview/TwoPlayerExampleProvider.java)
  - Shared `ViewProvider` by multiple `Player`, which shows how to open one provider to multiple players.
  - Allows players to have their own `PlayerView`/inventory but the same logic provided by the `ViewProvider`.

## Template

This project is a copy of [minestom-library-template](https://github.com/Koboo/minestom-library-template).

## Links

- [Minestom](https://minestom.net)