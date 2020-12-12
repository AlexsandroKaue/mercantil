/*
 * package com.frontbackend.thymeleaf.bootstrap.model.paging;
 * 
 * import java.util.Comparator; import java.util.HashMap; import java.util.Map;
 * 
 * import com.frontbackend.thymeleaf.bootstrap.model.paging.Direction; import
 * com.kaue.model.Produto;
 * 
 * import lombok.AllArgsConstructor; import lombok.EqualsAndHashCode; import
 * lombok.Getter;
 * 
 * public final class ProdutoComparators {
 * 
 * @EqualsAndHashCode
 * 
 * @AllArgsConstructor
 * 
 * @Getter static class Key { Key(String name, Direction dir) { this.name =
 * name; this.dir = dir; } String name; Direction dir; }
 * 
 * static Map<Key, Comparator<Produto>> map = new HashMap<>();
 * 
 * static { map.put(new Key("codigo", Direction.asc),
 * Comparator.comparing(Produto::getCodigo)); map.put(new Key("codigo",
 * Direction.desc), Comparator.comparing(Produto::getCodigo) .reversed());
 * 
 * map.put(new Key("descricao", Direction.asc),
 * Comparator.comparing(Produto::getDescricao)); map.put(new Key("descricao",
 * Direction.desc), Comparator.comparing(Produto::getDescricao) .reversed());
 * 
 * map.put(new Key("marca", Direction.asc),
 * Comparator.comparing(Produto::getMarca)); map.put(new Key("marca",
 * Direction.desc), Comparator.comparing(Produto::getMarca) .reversed());
 * 
 * map.put(new Key("preco", Direction.asc),
 * Comparator.comparing(Produto::getValorDeVenda)); map.put(new Key("preco",
 * Direction.desc), Comparator.comparing(Produto::getValorDeVenda) .reversed());
 * }
 * 
 * public static Comparator<Produto> getComparator(String name, Direction dir) {
 * return map.get(new Key(name, dir)); }
 * 
 * private ProdutoComparators() { } }
 */