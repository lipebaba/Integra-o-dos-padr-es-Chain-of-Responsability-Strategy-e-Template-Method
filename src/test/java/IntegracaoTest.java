import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class IntegracaoTest {
    private final Triagem cadeia = new TriagemPapel(new TriagemPlastico(null));
    @Test void papelEconomico() { assertEquals("Residuo: Caixa | Separar e compactar papel | Custo: R$ 10.00", cadeia.atender(new Residuo("Caixa", "papel", 5), new CustoEconomico())); }
    @Test void plasticoExpressoPassaAoSegundoResponsavel() { assertEquals("Residuo: Garrafa | Separar e triturar plastico | Custo: R$ 12.00", cadeia.atender(new Residuo("Garrafa", "PLASTICO", 3), new CustoExpresso())); }
    @Test void tipoNaoAtendido() { assertEquals("Residuo sem processamento disponivel", cadeia.atender(new Residuo("Lata", "metal", 1), new CustoEconomico())); }
    @Test void trocaEstrategia() {
        Processamento p = new ProcessamentoPapel(new CustoEconomico()); Residuo r = new Residuo("Caixa", "papel", 2);
        assertTrue(p.processar(r).endsWith("R$ 4.00")); p.setEstrategia(new CustoExpresso()); assertTrue(p.processar(r).endsWith("R$ 8.00"));
    }
    @Test void rejeitaDadosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new Residuo("X", "papel", 0));
        assertThrows(IllegalArgumentException.class, () -> new Residuo("X", "papel", Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new Residuo("", "papel", 1));
    }
}
