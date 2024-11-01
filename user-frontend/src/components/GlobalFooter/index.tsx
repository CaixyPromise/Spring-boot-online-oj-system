import "./index.css"
import {Separator} from "@/components/ui/separator";
import {DEFAULT_SETTINGS} from "../../../config/config";
import {Icons} from "@/components/ui/icons";

export default function GlobalFooter()
{
    const currentYear = new Date().getFullYear();

    return (
        <>
            <Separator />
            <div className="global-footer">
                <div>{DEFAULT_SETTINGS.author} © {currentYear} {DEFAULT_SETTINGS.title}</div>
                <div style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                }}>
                    <Icons.Github style={{width: "18px", height: "18px"}} />
                    <a href="https://www.github.com/CaixyPromise" target="_blank" style={{marginLeft:"8px"}}>
                        CaixyPromise
                    </a>
                </div>
            </div>
        </>
    );
}