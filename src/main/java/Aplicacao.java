import java.util.Locale;
import java.util.Objects;

class Residuo {
    final String nome, tipo; final double peso;
    Residuo(String nome, String tipo, double peso) {
        if (nome == null || nome.trim().isEmpty() || tipo == null || tipo.trim().isEmpty() || !Double.isFinite(peso) || peso <= 0) throw new IllegalArgumentException("Dados invalidos");
        this.nome = nome; this.tipo = tipo; this.peso = peso;
    }
}
interface EstrategiaCusto { double calcular(double peso); }
class CustoEconomico implements EstrategiaCusto { public double calcular(double peso) { return peso * 2; } }
class CustoExpresso implements EstrategiaCusto { public double calcular(double peso) { return peso * 4; } }
abstract class Processamento {
    private EstrategiaCusto estrategia;
    Processamento(EstrategiaCusto estrategia) { setEstrategia(estrategia); }
    void setEstrategia(EstrategiaCusto estrategia) { this.estrategia = Objects.requireNonNull(estrategia); }
    final String processar(Residuo r) {
        Objects.requireNonNull(r);
        return "Residuo: " + r.nome + " | " + preparar() + " | Custo: R$ " + String.format(Locale.US, "%.2f", estrategia.calcular(r.peso));
    }
    abstract String preparar();
}
class ProcessamentoPapel extends Processamento {
    ProcessamentoPapel(EstrategiaCusto e) { super(e); }
    String preparar() { return "Separar e compactar papel"; }
}
class ProcessamentoPlastico extends Processamento {
    ProcessamentoPlastico(EstrategiaCusto e) { super(e); }
    String preparar() { return "Separar e triturar plastico"; }
}
abstract class Triagem {
    private final Triagem proxima;
    Triagem(Triagem proxima) { this.proxima = proxima; }
    String atender(Residuo r, EstrategiaCusto e) {
        Objects.requireNonNull(r); Objects.requireNonNull(e);
        if (aceita(r)) return criarProcessamento(e).processar(r);
        return proxima == null ? "Residuo sem processamento disponivel" : proxima.atender(r, e);
    }
    abstract boolean aceita(Residuo r);
    abstract Processamento criarProcessamento(EstrategiaCusto e);
}
class TriagemPapel extends Triagem {
    TriagemPapel(Triagem proxima) { super(proxima); }
    boolean aceita(Residuo r) { return "papel".equalsIgnoreCase(r.tipo.trim()); }
    Processamento criarProcessamento(EstrategiaCusto e) { return new ProcessamentoPapel(e); }
}
class TriagemPlastico extends Triagem {
    TriagemPlastico(Triagem proxima) { super(proxima); }
    boolean aceita(Residuo r) { return "plastico".equalsIgnoreCase(r.tipo.trim()); }
    Processamento criarProcessamento(EstrategiaCusto e) { return new ProcessamentoPlastico(e); }
}
public class Aplicacao {
    public static void main(String[] args) {
        Triagem cadeia = new TriagemPapel(new TriagemPlastico(null));
        System.out.println(cadeia.atender(new Residuo("Caixas", "papel", 5), new CustoEconomico()));
        System.out.println(cadeia.atender(new Residuo("Garrafas", "plastico", 3), new CustoExpresso()));
        System.out.println(cadeia.atender(new Residuo("Latas", "metal", 2), new CustoEconomico()));
    }
}
