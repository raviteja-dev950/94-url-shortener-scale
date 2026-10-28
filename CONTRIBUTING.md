# Contributing to 94-url-shortener-scale

Thank you for considering contributing to Tier 10 System Design project!

## How to Contribute
1. Fork repo https://github.com/raviteja-dev950/94-url-shortener-scale
2. Create branch: `git checkout -b feature/your-feature`
3. Commit: `git commit -m "feat: add your feature"`
4. Push: `git push origin feature/your-feature`
5. Open PR

## Development Setup
- Java 17
- Spring Boot 3.2.5
- Eclipse/STS or IntelliJ
- Maven 3.8+
- No Lombok (Pure Java)

## Code Style
- Follow existing package structure: entity, repository, service, controller, config
- No Lombok
- Add JUnit tests for new features
- Update README with new endpoints

## Scale Improvements Welcome
- Redis instead of Caffeine for distributed cache
- Postgres + Elasticsearch for analytics
- Rate Limiting per IP (Bucket4j)
- Custom short codes
- Expiry for links

## Reporting Issues
Open issue with Tier 10 label.

## License
By contributing, you agree MIT License.

## Author
Ravi Teja - 94/102 Roadmap
